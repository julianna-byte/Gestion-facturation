package Util;

import java.math.BigDecimal;

public class NombreEnLettresUtil {

 private static final String[] UNITES = {
            "", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf",
            "dix", "onze", "douze", "treize", "quatorze", "quinze", "seize",
            "dix-sept", "dix-huit", "dix-neuf"
    };

     private static final String[] DIZAINES = {
            "", "dix", "vingt", "trente", "quarante", "cinquante", "soixante",
            "soixante-dix", "quatre-vingt", "quatre-vingt-dix"
    };

    private NombreEnLettresUtil() {
        // classe utilitaire, non instanciable
    }

    /**
     * Convertit un montant (BigDecimal) en toutes lettres.
     * Les décimales sont ignorées (arrondi à l'entier le plus proche),
     * conformément à la règle RG-01 (montants affichés sans décimale).
     */
    public static String convertir(BigDecimal montant) {
        if (montant == null) {
            return "zéro";
        }
        long valeur = montant.setScale(0, java.math.RoundingMode.HALF_UP).longValueExact();
        return convertir(valeur);
    }

    public static String convertir(long nombre) {
        if (nombre == 0) {
            return "zéro";
        }
        if (nombre < 0) {
            return "moins " + convertir(-nombre);
        }

        StringBuilder resultat = new StringBuilder();

        long milliards = nombre / 1_000_000_000L;
        long millions = (nombre % 1_000_000_000L) / 1_000_000L;
        long milliers = (nombre % 1_000_000L) / 1_000L;
        long reste = nombre % 1_000L;

        if (milliards > 0) {
            resultat.append(convertirCentaines(milliards))
                    .append(milliards > 1 ? " milliards " : " milliard ");
        }
        if (millions > 0) {
            resultat.append(convertirCentaines(millions))
                    .append(millions > 1 ? " millions " : " million ");
        }
        if (milliers > 0) {
            if (milliers == 1) {
                resultat.append("mille ");
            } else {
                resultat.append(convertirCentaines(milliers)).append(" mille ");
            }
        }
        if (reste > 0) {
            resultat.append(convertirCentaines(reste));
        }

        return capitaliser(resultat.toString().trim().replaceAll("\\s+", " "));
    }

    private static String convertirCentaines(long nombre) {
        StringBuilder sb = new StringBuilder();

        long centaines = nombre / 100;
        long resteCentaines = nombre % 100;

        if (centaines > 0) {
            if (centaines == 1) {
                sb.append("cent");
            } else {
                sb.append(UNITES[(int) centaines]).append(" cent");
                if (resteCentaines == 0) {
                    sb.append("s"); // "deux cents" mais "deux cent trois"
                }
            }
            if (resteCentaines > 0) {
                sb.append(" ");
            }
        }

        if (resteCentaines > 0) {
            sb.append(convertirDizaines(resteCentaines));
        }

        return sb.toString();
    }

    private static String convertirDizaines(long nombre) {
        if (nombre < 20) {
            return UNITES[(int) nombre];
        }

        long dizaine = nombre / 10;
        long unite = nombre % 10;

        // Cas particuliers : 70-79 et 90-99 (base vigésimale française)
        if (dizaine == 7 || dizaine == 9) {
            long base = dizaine - 1;
            long resteUnite = 10 + unite;
            String liaison = (resteUnite == 11 && dizaine == 7) ? "-et-" : "-";
            return DIZAINES[(int) base] + liaison + UNITES[(int) resteUnite];
        }

        StringBuilder sb = new StringBuilder(DIZAINES[(int) dizaine]);

        if (unite == 1 && dizaine != 8) {
            sb.append("-et-un");
        } else if (unite > 0) {
            sb.append("-").append(UNITES[(int) unite]);
        } else if (dizaine == 8) {
            sb.append("s"); // "quatre-vingts" sans reste
        }

        return sb.toString();
    }

    private static String capitaliser(String texte) {
        if (texte == null || texte.isEmpty()) {
            return texte;
        }
        return Character.toUpperCase(texte.charAt(0)) + texte.substring(1);
    }


}
