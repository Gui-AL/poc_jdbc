package com.api.poc_jdbc.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateUtils {

    private DateUtils() {
    }

    public static Date somaDias(Date dt, int dias) {
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, dias);
        return c.getTime();
    }

    public static String dataParaTexto(Date dt) {
        return dataParaTexto(dt, DateUtils.FMT_DATAHORA);
    }

    public static String dataParaTexto(Date dt, String format) {
        String ret = null;
        SimpleDateFormat mask = new SimpleDateFormat(format);
        ret = mask.format(dt);
        return ret;
    }

    public static Date textoParaData(String sData) {
        return textoParaData(sData, FMT_DATA);
    }

    public static Date textoParaDataHora(String sData) {
        return textoParaData(sData, FMT_DATAHORA);
    }

    public static Date textoParaData(String sData, String format) {
        Date ret = null;
        if (sData != null && format != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                ret = sdf.parse(sData);
            } catch (ParseException e) {
//                System.err.println("Erro convertendo texto para data:" + sData + " - " + format);
            }
        }
        return ret;
    }

    public static String formataData(Date data) throws Exception {
        return formataData(data, DateUtils.FMT_DATA_LONGO);
    }

    public static String formataData(Date data, String formato) throws Exception {
        String ret = null;
        if (data != null) {
            DateFormat formatter = new SimpleDateFormat(formato);
            ret = formatter.format(data);
        }
        return ret;
    }

    public static String formataData(String data, String formato) throws Exception {
        String ret = null;

        if (data != null && !data.isEmpty()) {
            DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = originalFormat.parse(data);
            DateFormat targetFormat = new SimpleDateFormat(formato);
            ret = targetFormat.format(parsedDate);
        }

        return ret;
    }

    public static List<String> getRangeDeDias(LocalDate dataComeco, LocalDate dataFim) {
        final int days = (int) dataComeco.until(dataFim, ChronoUnit.DAYS);
        return Stream.iterate(dataComeco, d -> d.plusDays(1))
                .limit(days)
                .sorted()
                .collect(Collectors.mapping(d -> d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), Collectors.toList()));
    }

    public static Map<Integer, Map<Integer, List<Object>>> getRangeDeDiasAgrupadoPorISOWeek(LocalDate dataComeco, LocalDate dataFim) {
        final Locale LOCALE = new Locale("pt", "BR");
        List<String> lista = getRangeDeDias(dataComeco, dataFim);

        Function<String, Integer> classifier = (d) -> (Integer) LocalDate.parse(d, DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE)).get(WeekFields.ISO.weekOfYear());
        Function<String, Integer> classifierYear = (d) -> (Integer) LocalDate.parse(d, DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE)).get(ChronoField.YEAR);
        return lista.stream().collect(
                Collectors.groupingBy(classifierYear,
                        Collectors.groupingBy(classifier
                                , LinkedHashMap::new, Collectors.mapping(p -> p, Collectors.toList())))

        );
    }

    public static Map<Integer, Map<Integer, List<Object>>> getRangeDeDiasAgrupadoPorWeekCoopeflora(LocalDate dataComeco, LocalDate dataFim) {
        final Locale LOCALE = new Locale("pt", "BR");
        List<String> lista = getRangeDeDias(dataComeco, dataFim);

        Function<String, LocalDate> stringToDate = (s) -> LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE));

        Function<String, Integer> classifier = (d) -> stringToDate.apply(d).get(WeekFields.of(LOCALE).weekOfYear());
        Function<String, Integer> classifierYear = (d) -> classifier.apply(d) >= 52 && stringToDate.apply(d).getDayOfMonth() < 4
                ? stringToDate.apply(d).get(ChronoField.YEAR) - 1 : stringToDate.apply(d).get(ChronoField.YEAR);
        return lista.stream().collect(
                Collectors.groupingBy(classifierYear,
                        Collectors.groupingBy(classifier
                                , LinkedHashMap::new, Collectors.mapping(p -> p, Collectors.toList())))

        );
    }

    public static Map<Integer, Map<Integer, List<Object>>> getRangeDeDiasAgrupadoPorWeek(LocalDate dataComeco, LocalDate dataFim) {
        final Locale LOCALE = new Locale("pt", "BR");
        List<String> lista = getRangeDeDias(dataComeco, dataFim);

        Function<String, Integer> classifier = (d) -> (Integer) LocalDate.parse(d, DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE)).get(WeekFields.of(LOCALE).weekOfYear());
        Function<String, Integer> classifierYear = (d) -> (Integer) LocalDate.parse(d, DateTimeFormatter.ofPattern("dd/MM/yyyy", LOCALE)).get(ChronoField.YEAR);
        return lista.stream().collect(
                Collectors.groupingBy(classifierYear,
                        Collectors.groupingBy(classifier
                                , LinkedHashMap::new, Collectors.mapping(p -> p, Collectors.toList())))

        );
    }

    public static String diaSemanaDescricao(int _diaSemana) {

        switch (_diaSemana) {
            case 1:
                return "Domingo";
            case 2:
                return "Segunda";
            case 3:
                return "Terca";
            case 4:
                return "Quarta";
            case 5:
                return "Quinta";
            case 6:
                return "Sexta";
            case 7:
                return "Sabado";
            default:
                return
                        String.format("Dia da semana [%s] n[\u00e3]o encontrado para convers\u00e3o", _diaSemana);
        }

    }

    public static Map<Integer, List<String>> getRangeDeDiasAgrupadoPorSemana(LocalDate dataComeco, LocalDate dataFim) {
        final Locale LOCALE = new Locale("pt", "BR");
        List<String> lista = getRangeDeDias(dataComeco, dataFim);

        Function<String, Integer> classifier = (d) -> (Integer) LocalDate.parse(d, DateTimeFormatter.ofPattern("dd/MM/yyyy")).get(WeekFields.of(LOCALE).weekOfYear());
        return lista.stream().collect(
                Collectors.groupingBy(classifier
                        , LinkedHashMap::new, Collectors.mapping(p -> p, Collectors.toList())));
    }

    /**
     * recupera proxima data conforme dia da semana desejado
     *
     * @param dtRef       data inicio para referencia
     * @param diaDesejado proximo dia da semana desejado
     * @return
     */
    public static Date proximoDiaSemana(Date dtRef, int diaDesejado) {
        int diaRef = LocalDate.parse(DateUtils.dataParaTexto(dtRef, DateUtils.FMT_DATA_LONGO), DateTimeFormatter.ofPattern(DateUtils.FMT_DATA_LONGO)).getDayOfWeek().getValue();
        int diff = 7;
        if (diaRef > diaDesejado) {
            diff = 7 - (diaRef - diaDesejado);
        } else if (diaRef < diaDesejado) {
            diff = diaDesejado - diaRef;
        }
        return DateUtils.somaDias(dtRef, diff);
    }

    public static int retornarDiaSemana(Date dt) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(dt);
        int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);

        return diaSemana;
    }

    public static Date proximoDia(Date dt, int dia) {
        int diaSemana = retornarDiaSemana(dt);
        int diasDiferentes = (7 - diaSemana) + dia;
        return somaDias(dt, diasDiferentes);
    }

    /**
     * formato: dd/MM/yy HH:mm
     */
    public static String FMT_DATAHORA = "dd/MM/yy HH:mm";
    /**
     * formato: dd/MM/yyyy HH:mm
     */
    public static String FMT_DATALONGO_HORA = "dd/MM/yyyy HH:mm";

    /**
     * formato: dd/MM/yy
     */
    public static String FMT_DATA = "dd/MM/yy";
    /**
     * formato: dd/MM/yyyy
     */
    public static String FMT_DATA_LONGO = "dd/MM/yyyy";
    /**
     * formato: HH:mm:ss
     */
    public static String FMT_HORA = "HH:mm:ss";
    /**
     * formato: yyyyMMdd
     */
    public static String FMT_DATA_YMD = "yyyyMMdd";
    /**
     * formato: yyyy-MM-dd
     */
    public static String FMT_DATA_YMD_LONGO = "yyyy-MM-dd";
    /**
     * formato: ddMMyy
     */
    public static String FMT_DATA_DMY = "ddMMyy";
    /**
     * formato: HHmmss
     */
    public static String FMT_HORA_NODIVIDER = "HHmmss";
    /**
     * formato: yyyyMMdd_HHmmss
     */
    public static String FMT_DATAHORA_NODIVIDER = "yyyyMMdd_HHmmss";

}
