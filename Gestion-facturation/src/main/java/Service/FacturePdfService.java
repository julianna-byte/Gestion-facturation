package Service;

import Entity.Facture;
import Entity.BonCommande;
import Entity.Clients;
import Entity.LignesCommande;
import Repository.FactureRepository;
import Util.NombreEnLettresUtil;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

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

    public FacturePdfService(FactureRepository factureRepository) {
        this.factureRepository = factureRepository;
    }
      public byte[] genererPdf(Long idFacture) {
        Facture facture = factureRepository.findById(idFacture)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));

        BonCommande bc = facture.getBonCommande();
        Clients client = facture.getClients();

        try {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();

            ajouterEntete(document, facture);
            ajouterBlocClient(document, client);
            ajouterTableauLignes(document, bc.getLignes());
            ajouterRecapitulatif(document, bc);
            ajouterMontantEnLettres(document, bc.getTotalTtc());
            ajouterMentionsLegales(document);

            document.close();
            return baos.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    private void ajouterEntete(Document document, Facture facture) throws DocumentException {
        try {
            Image logo = Image.getInstance(
                    getClass().getClassLoader().getResource("static/logo.png"));
            logo.scaleToFit(120, 60);
            document.add(logo);
        } catch (Exception e) {
            // logo optionnel si le fichier est absent
        }

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        document.add(new Paragraph("OSSAN ASUR SARL", titleFont));

        Font infoFont = new Font(Font.HELVETICA, 9);
        document.add(new Paragraph(
                "RC TG-LFW-01-2021-B12-01539, NIF : 1001198231, 22 BP 221 Lomé-Togo",
                infoFont));
        document.add(Chunk.NEWLINE);

        Font numFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        document.add(new Paragraph("Facture N° " + facture.getNumerofacture(), numFont));
        document.add(new Paragraph("Date : " + facture.getDateCreation()));
        document.add(new Paragraph("Type : " + facture.getType()));
        document.add(Chunk.NEWLINE);
    }

    private void ajouterBlocClient(Document document, Clients client) throws DocumentException {
        Font labelFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        document.add(new Paragraph("Facturé à :", labelFont));
        document.add(new Paragraph(client.getRaisonsociale()));
        document.add(new Paragraph("NIF : " + client.getNIF()));
        document.add(new Paragraph(client.getAdresse() + ", " + client.getVille() + " - " + client.getPays()));
        if (client.getTelephone() != null) {
            document.add(new Paragraph("Tél : " + client.getTelephone()));
        }
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
        ajouterLigneRecap(recap, "TVA (18%)", bc.getTva());
        ajouterLigneRecap(recap, "Total TTC", bc.getTotalTtc());

        document.add(recap);
        document.add(Chunk.NEWLINE);
    }

    private void ajouterLigneRecap(PdfPTable table, String label, BigDecimal montant) {
        table.addCell(new PdfPCell(new Phrase(label)));
        PdfPCell valeur = new PdfPCell(new Phrase(formatMontant(montant) + " XOF"));
        valeur.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valeur);
    }

    private void ajouterMontantEnLettres(Document document, BigDecimal montant) throws DocumentException {
        String enLettres = NombreEnLettresUtil.convertir(montant);
        document.add(new Paragraph(
                "Arrêtée la présente facture à la somme de : " + enLettres + " francs CFA."));
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
