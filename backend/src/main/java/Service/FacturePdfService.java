package Service;

import Entity.Facture;
import Entity.BonCommande;
import Entity.Clients;
import Entity.LignesCommande;
import Entity.Reglement;
import Entity.TypeFacture;
import Repository.FactureRepository;
import Util.NombreEnLettresUtil;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class FacturePdfService {

    private final FactureRepository factureRepository;

    @org.springframework.beans.factory.annotation.Value("${app.devise:XOF}")
    private String devise;

    public FacturePdfService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }

    public byte[] genererPdf(Long idFacture, Boolean inclureSuiviPaiement) {
        Facture facture = factureRepository.findById(idFacture)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));

        BonCommande bc = facture.getBonCommande();
        Clients client = facture.getClients();

        boolean afficherSuivi = inclureSuiviPaiement != null
                ? inclureSuiviPaiement
                : facture.getType() == TypeFacture.DEFINITIVE;

        try {
            // 1) Generation du CONTENU seul (sans entete/pied de page) 

            ByteArrayOutputStream contenuBaos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 40, 40, 105, 70);
            PdfWriter.getInstance(document, contenuBaos);
            document.open();

            ajouterInfosFacture(document, facture);
            ajouterBlocClient(document, client);
            ajouterPhraseIntro(document);
            ajouterTableauLignes(document, bc.getLignes());
            ajouterRecapitulatif(document, bc);

            if (afficherSuivi) {
                ajouterSuiviPaiement(document, facture);
            }

            ajouterConditions(document, facture);
            ajouterMontantEnLettres(document, bc.getTotalTtc());
            ajouterMentionsLegales(document);

            document.close();

            return fusionnerAvecPapierEntete(contenuBaos.toByteArray());

        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    private byte[] fusionnerAvecPapierEntete(byte[] contenuPdf) {
        try (InputStream templateStream = getClass().getClassLoader()
                .getResourceAsStream("static/PapierEntete.pdf")) {

            if (templateStream == null) {

                return contenuPdf;
            }

            PdfReader templateReader = new PdfReader(templateStream);
            PdfReader contenuReader = new PdfReader(contenuPdf);

            ByteArrayOutputStream finalBaos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(contenuReader, finalBaos);

            int nbPages = contenuReader.getNumberOfPages();
            for (int i = 1; i <= nbPages; i++) {
                // La page 1 du papier a en-tete est reutilisee en fond sur
                // chaque page generee (meme en-tete/pied de page partout).
                PdfImportedPage pageTemplate = stamper.getImportedPage(templateReader, 1);
                PdfContentByte fond = stamper.getUnderContent(i);
                fond.addTemplate(pageTemplate, 0, 0);
            }

            stamper.close();
            return finalBaos.toByteArray();

        } catch (Exception e) {
            // En cas de probleme avec le template, on ne bloque pas la
            // generation : le contenu seul est renvoye.
            return contenuPdf;
        }
    }

    private void ajouterInfosFacture(Document document, Facture facture) throws DocumentException {
        Font numFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font dateFont = new Font(Font.HELVETICA, 10);

        PdfPTable ligneEntete = new PdfPTable(2);
        ligneEntete.setWidthPercentage(100);
        ligneEntete.setWidths(new float[]{2, 1});

        PdfPCell celluleNumero = new PdfPCell(new Phrase("Facture N° " + facture.getNumerofacture(), numFont));
        celluleNumero.setBorder(Rectangle.NO_BORDER);
        ligneEntete.addCell(celluleNumero);

        PdfPCell celluleDate = new PdfPCell(new Phrase("Date : " + facture.getDateCreation(), dateFont));
        celluleDate.setBorder(Rectangle.NO_BORDER);
        celluleDate.setHorizontalAlignment(Element.ALIGN_RIGHT);
        ligneEntete.addCell(celluleDate);

        document.add(ligneEntete);
        document.add(new Paragraph("Type : " + facture.getType()));

        Color couleurStatut;
        switch (facture.getStatut()) {
            case PAYEE -> couleurStatut = new Color(46, 125, 50);
            case PARTIELLEMENT_PAYEE -> couleurStatut = new Color(230, 126, 34);
            case ANNULEE -> couleurStatut = new Color(192, 57, 43);
            default -> couleurStatut = new Color(80, 80, 80);
        }
        Font statutFont = new Font(Font.HELVETICA, 11, Font.BOLD, couleurStatut);
        document.add(new Paragraph("Statut : " + libelleStatut(facture.getStatut()), statutFont));

        if (facture.getStatut() == Entity.StatutFacture.ANNULEE
                && facture.getMotifAnnulation() != null) {
            document.add(new Paragraph("Motif d'annulation : " + facture.getMotifAnnulation()));
        }
        document.add(Chunk.NEWLINE);
    }

    private String libelleStatut(Entity.StatutFacture statut) {
        return switch (statut) {
            case EMISE -> "Émise (en attente de règlement)";
            case PARTIELLEMENT_PAYEE -> "Partiellement payée";
            case PAYEE -> "Payée intégralement";
            case ANNULEE -> "Annulée";
        };
    }

    private void ajouterBlocClient(Document document, Clients client) throws DocumentException {
        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD | Font.UNDERLINE);
        Font valeurFont = new Font(Font.HELVETICA, 10);

        document.add(ligneLabelValeur("Client :", client.getRaisonsociale(), labelFont, valeurFont));
        document.add(ligneLabelValeur("RCCM :", client.getRCCM(), labelFont, valeurFont));
        document.add(ligneLabelValeur("NIF :", client.getNIF(), labelFont, valeurFont));
        document.add(ligneLabelValeur("Adresse :",
                client.getAdresse() + ", " + client.getVille() + " - " + client.getPays(),
                labelFont, valeurFont));
        if (client.getTelephone() != null) {
            document.add(ligneLabelValeur("Téléphone :", client.getTelephone(), labelFont, valeurFont));
        }
        document.add(Chunk.NEWLINE);
    }

    private Paragraph ligneLabelValeur(String label, String valeur, Font labelFont, Font valeurFont) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label + " ", labelFont));
        p.add(new Chunk(valeur != null ? valeur : "", valeurFont));
        return p;
    }

    private void ajouterPhraseIntro(Document document) throws DocumentException {
        Font introFont = new Font(Font.HELVETICA, 10, Font.ITALIC);
        document.add(new Paragraph(
                "Nous avons le plaisir de vous transmettre sur votre demande notre meilleure offre",
                introFont));
        document.add(Chunk.NEWLINE);
    }

    private void ajouterTableauLignes(Document document, List<LignesCommande> lignes) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 1.5f, 1.5f, 1.5f});

        Stream.of("Article", "Qté", "PU HT", "Remise", "Total HT")
                .forEach(h -> {
                    PdfPCell cell = new PdfPCell(new Phrase(h, new Font(Font.HELVETICA, 10, Font.BOLD)));
                    cell.setBackgroundColor(new Color(237, 125, 49));
                    cell.setPadding(5);
                    table.addCell(cell);
                });

        for (LignesCommande ligne : lignes) {
            BigDecimal remise = ligne.getRemise() != null ? ligne.getRemise() : BigDecimal.ZERO;
            BigDecimal totalLigne = ligne.getPrixunitaire()
                    .multiply(BigDecimal.valueOf(ligne.getQuantite()))
                    .subtract(remise);

            table.addCell(ligne.getArticles().getLibelle());
            table.addCell(String.valueOf(ligne.getQuantite()));
            table.addCell(formatMontant(ligne.getPrixunitaire()));
            table.addCell(formatMontant(remise));
            table.addCell(formatMontant(totalLigne));
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
    }

    private void ajouterRecapitulatif(Document document, BonCommande bc) throws DocumentException {
        PdfPTable recap = new PdfPTable(2);
        recap.setWidthPercentage(40);
        recap.setHorizontalAlignment(Element.ALIGN_RIGHT);

        ajouterLigneRecap(recap, "Total HT", bc.getTotalHT());
        ajouterLigneRecap(recap, "TVA", bc.getTva());
        ajouterLigneRecap(recap, "Total TTC", bc.getTotalTtc());

        document.add(recap);
        document.add(Chunk.NEWLINE);
    }

    private void ajouterLigneRecap(PdfPTable table, String label, BigDecimal montant) {
        table.addCell(new PdfPCell(new Phrase(label)));
        PdfPCell valeur = new PdfPCell(new Phrase(formatMontant(montant) + " " + devise));
        valeur.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valeur);
    }

    private void ajouterSuiviPaiement(Document document, Facture facture) throws DocumentException {
        List<Reglement> reglements = facture.getReglements();

        BigDecimal montantPaye = reglements == null ? BigDecimal.ZERO
                : reglements.stream()
                        .map(Reglement::getMontant)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal resteAPayer = facture.getTotalTtc().subtract(montantPaye);
        if (resteAPayer.compareTo(BigDecimal.ZERO) < 0) {
            resteAPayer = BigDecimal.ZERO;
        }

        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        document.add(new Paragraph("Suivi du règlement", labelFont));

        PdfPTable suivi = new PdfPTable(2);
        suivi.setWidthPercentage(40);
        suivi.setHorizontalAlignment(Element.ALIGN_RIGHT);
        ajouterLigneRecap(suivi, "Montant payé", montantPaye);
        ajouterLigneRecap(suivi, "Reste à payer", resteAPayer);
        document.add(suivi);
        document.add(Chunk.NEWLINE);

        if (reglements != null && !reglements.isEmpty()) {
            document.add(new Paragraph("Historique des règlements :", labelFont));

            PdfPTable histo = new PdfPTable(3);
            histo.setWidthPercentage(100);
            histo.setWidths(new float[]{1.5f, 1.5f, 2});

            Stream.of("Date", "Montant", "Mode")
                    .forEach(h -> {
                        PdfPCell cell = new PdfPCell(new Phrase(h, new Font(Font.HELVETICA, 9, Font.BOLD)));
                        cell.setBackgroundColor(new Color(230, 230, 230));
                        cell.setPadding(4);
                        histo.addCell(cell);
                    });

            for (Reglement r : reglements) {
                histo.addCell(String.valueOf(r.getDateReglement()));
                histo.addCell(formatMontant(r.getMontant()) + " " + devise);
                histo.addCell(r.getMode());
            }

            document.add(histo);
        }

        document.add(Chunk.NEWLINE);
    }

    private void ajouterConditions(Document document, Facture facture) throws DocumentException {
        Font titreFont = new Font(Font.HELVETICA, 10, Font.BOLD | Font.UNDERLINE);
        Font valeurFont = new Font(Font.HELVETICA, 10);

        document.add(new Paragraph("Conditions :", titreFont));

        String personnalise = facture.getConditionsPersonnalisees();
        if (personnalise != null && !personnalise.isBlank()) {
            for (String ligne : personnalise.split("\n")) {
                if (!ligne.isBlank()) {
                    document.add(new Paragraph(ligne.trim(), valeurFont));
                }
            }
        }
        document.add(Chunk.NEWLINE);
    }

    private void ajouterMontantEnLettres(Document document, BigDecimal montant) throws DocumentException {
        String enLettres = NombreEnLettresUtil.convertir(montant);
        Font montantFont = new Font(Font.HELVETICA, 10, Font.BOLD | Font.UNDERLINE);
        document.add(new Paragraph(
                "Arrêtée la présente facture à la somme de : " + enLettres + " francs CFA.",
                montantFont));
        document.add(Chunk.NEWLINE);
    }

    private void ajouterMentionsLegales(Document document) throws DocumentException {
        Font small = new Font(Font.HELVETICA, 8);
        document.add(new Paragraph(
                "Facture émise conformément aux règles en vigueur. Paiement à réception, sauf accord contraire.",
                small));
    }

    private String formatMontant(BigDecimal montant) {
        NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
        nf.setMaximumFractionDigits(0); // RG-01 : montants sans decimale
        return nf.format(montant);
    }
}