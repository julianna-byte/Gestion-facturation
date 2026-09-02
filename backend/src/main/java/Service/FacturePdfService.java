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
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.ColumnText;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class FacturePdfService {

   private final FactureRepository factureRepository;

    // RG-01 : devise paramétrable
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
            Document document = new Document(PageSize.A4, 36, 36, 100, 65);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            writer.setPageEvent(new EnteteEtPiedDePage());

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
            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
    private static class EnteteEtPiedDePage extends PdfPageEventHelper {

        private static final Color ORANGE = new Color(237, 125, 49);
        private static final Color NAVY_FOOTER = new Color(20, 25, 35);
        private static final float LOGO_LARGEUR_MAX = 120;
        private static final float LOGO_HAUTEUR_MAX = 50;

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            float largeurPage = document.getPageSize().getWidth();
            float hautPage = document.getPageSize().getHeight();

            // EN-TETE 
            float logoLargeurReelle = LOGO_LARGEUR_MAX; // valeur de repli si le logo est absent
            try {
                Image logo = Image.getInstance(
                        FacturePdfService.class.getClassLoader().getResource("static/Logotype_Ossan_Asur_Noir.png"));

                logo.scaleToFit(LOGO_LARGEUR_MAX, LOGO_HAUTEUR_MAX);
                logoLargeurReelle = logo.getScaledWidth();
                float logoHauteurReelle = logo.getScaledHeight();

                float logoY = hautPage - 20 - logoHauteurReelle;
                logo.setAbsolutePosition(36, logoY);
                cb.addImage(logo);
            } catch (Exception e) {
                // logo optionnel si le fichier est absent
            }

            float xSeparateur = 36 + logoLargeurReelle + 15;
            cb.setColorStroke(ORANGE);
            cb.setLineWidth(1.5f);
            cb.moveTo(xSeparateur, hautPage - 18);
            cb.lineTo(xSeparateur, hautPage - 65);
            cb.stroke();

            Font sloganTitreFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font sloganSousTitreFont = new Font(Font.HELVETICA, 7, Font.ITALIC, Color.DARK_GRAY);

            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    new Phrase("AUDIT - OPTIMISATION - DIGITALISATION", sloganTitreFont),
                    largeurPage - 36, hautPage - 28, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    new Phrase("INFRASTRUCTURE - FORMATIONS", sloganTitreFont),
                    largeurPage - 36, hautPage - 39, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    new Phrase("OSSAN ASUR, le Digital au service de l'excellence opérationnelle", sloganSousTitreFont),
                    largeurPage - 36, hautPage - 51, 0);

            cb.setColorStroke(ORANGE);
            cb.setLineWidth(2f);
            cb.moveTo(0, hautPage - 85);
            cb.lineTo(largeurPage, hautPage - 85);
            cb.stroke();

            //  PIED DE PAGE
            cb.setColorFill(NAVY_FOOTER);
            cb.rectangle(0, 34, largeurPage, 26);
            cb.fill();

            Font footerFont = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.WHITE);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("+228 22 51 58 52 | 70 25 46 46 | 97 06 08 93   •   ossanasur@ossanasur.com", footerFont),
                    largeurPage / 2, 51, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("ADIDOGOME, 2ème von à droite après la rétention d'eau, sur la route de Ségbé", footerFont),
                    largeurPage / 2, 40, 0);

            Font mentionsFont = new Font(Font.HELVETICA, 7, Font.NORMAL, Color.BLACK);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("OSSAN ASUR SARL, RC TG-LFW-01-2021-B12-01539, NIF : 1001198231, 22 BP 221 Lomé-Togo", mentionsFont),
                    largeurPage / 2, 20, 0);

            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("Page " + writer.getPageNumber(), mentionsFont),
                    largeurPage / 2, 10, 0);
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

    // Phrase d'introduction, juste au-dessus du tableau des lignes
    private void ajouterPhraseIntro(Document document) throws DocumentException {
        Font introFont = new Font(Font.HELVETICA, 10, Font.ITALIC);
        document.add(new Paragraph(
                "Nous avons le plaisir de vous transmettre sur votre demande, notre meilleure offre",
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
        return NumberFormat.getInstance(Locale.FRANCE).format(montant);
    }
}