package com.CETSM.oficina_TCC.util;

/**
 * UTILITÁRIO DE VALIDAÇÃO DE CPF E CNPJ
 * Valida os dígitos verificadores oficiais de documentos brasileiros.
 */
public class CpfCnpjUtils {

    public static boolean isCpfCnpjValido(String valor) {
        if (valor == null) return false;
        // Remove caracteres não numéricos (pontos, traços, barras)
        String numeros = valor.replaceAll("\\D", "");

        if (numeros.length() == 11) {
            return isCpf(numeros);
        } else if (numeros.length() == 14) {
            return isCnpj(numeros);
        }
        return false;
    }

    private static boolean isCpf(String cpf) {
        if (cpf.matches("(\\d)\\1{10}")) return false;
        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpf.charAt(i) - '0') * (10 - i);
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 > 9) digito1 = 0;

            if (digito1 != (cpf.charAt(9) - '0')) return false;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 > 9) digito2 = 0;

            return digito2 == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isCnpj(String cnpj) {
        if (cnpj.matches("(\\d)\\1{13}")) return false;
        try {
            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += (cnpj.charAt(i) - '0') * peso1[i];
            }
            int digito1 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

            if (digito1 != (cnpj.charAt(12) - '0')) return false;

            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += (cnpj.charAt(i) - '0') * peso2[i];
            }
            int digito2 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

            return digito2 == (cnpj.charAt(13) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}