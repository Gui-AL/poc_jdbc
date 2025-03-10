package com.api.poc_jdbc.utils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.api.poc_jdbc.type.Registro;


public class RegistroUtils {

    private RegistroUtils() {
    }

    public static Registro procura(String chave, Object valor, List<Registro> lista) {

        for (Registro r : lista) {
            if (r.containsKey(chave)) {
                Object o = r.get(chave);
                if (o != null) {
                    if (o.getClass() == valor.getClass() && (o.equals(valor))) {
                        return r;
                    } // fim verificacao tipo da classe
                }
            } // fim verificacao se contem chave
        } // fim loop registros
        return null;
    }

    public static Registro converter(JSONObject obj) {
        Registro ret = new Registro();
        try {
            ret = new ObjectMapper().readValue(obj.toString(), Registro.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String[] listaKeys(Registro reg) {
        String[] akeys = null;
        if (reg != null) {
            Set<String> keys = reg.keySet();
            akeys = new String[keys.size()];
            int i = 0;
            for (String k : keys) {
                akeys[i++] = k;
            }
        }
        return akeys;
    }

    public static Registro converter(Object obj) {
        if (obj instanceof Registro)
            return (Registro) obj;
        return converter(obj, false);
    }

    public static Registro converter(Object obj, boolean isLower) {
        Registro ret = new Registro();
        Method[] allMethods = (obj.getClass()).getDeclaredMethods();

        for (Method method : allMethods) {
            try {
                if (method.getName().startsWith("get")) {
                    Object value = method.invoke(obj);
                    String nomeCampo = method.getName().subSequence(3, method.getName().length()).toString();
                    ret.put((isLower ? RegistroUtils.firstCharLower(nomeCampo) : nomeCampo), value);
                }

            } catch (IllegalAccessException | InvocationTargetException e) {
                System.err.println("Could not determine method: " + method.getName());
            } catch (Exception e) {
                System.err.println("-- erro convertendo objeto para registro ---");
            }
        }

        return ret;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Registro mapToRegistro(Map<String, Object> from) {
        if (from == null)
            return null;

        Registro reg = new Registro();
        for (Map.Entry<String, Object> entry : from.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                reg.put(entry.getKey(), mapToRegistro((Map) obj));
            } else if (obj instanceof List) {
                reg.put(entry.getKey(), ((List) entry.getValue()).stream().map(o -> mapToRegistro((Map) o)).collect(Collectors.toList()));
            } else {
                reg.put(entry.getKey(), entry.getValue());
            }
        }
        return reg;
    }

    public static String firstCharLower(String str) {
        char[] c = str.toCharArray();
        // apenas se primeiro caracter entre 'A' e 'Z'
        c[0] += (c[0] > 64 && c[0] < 91 ? 32 : 0);
        return new String(c);
    }

    @SuppressWarnings("unchecked")
    public static double[] getAsDoubleArray(Object o) {
        double[] d = {};
        if (o.getClass().isArray()) {
            if (o.getClass().getComponentType().getName().equalsIgnoreCase("double")) {
                d = Arrays.stream((double[]) o).toArray();
            } else if (o.getClass().getComponentType().getName().equalsIgnoreCase("int")) {
                d = Arrays.stream((int[]) o).asDoubleStream().toArray();
            } else if (o.getClass().getComponentType().getName().equalsIgnoreCase("java.math.BigDecimal")) {
                d = Arrays.stream((BigDecimal[]) o).mapToDouble(Number::doubleValue).toArray();
                //((List<BigDecimal>)o).stream().mapToDouble(i-> i.doubleValue()).toArray();
            } else if (o.getClass().getComponentType().getName().equalsIgnoreCase("java.lang.String")) {
                d = Arrays.stream((String[]) o).mapToDouble(Double::valueOf).toArray();
                //((List<BigDecimal>)o).stream().mapToDouble(i-> i.doubleValue()).toArray();
            } else {
                System.err.println(">>> tipo da lista nao pode ser convertido para double[]:" + o.getClass().getName());
            }
        } else if (o.getClass() == ArrayList.class) {
            List<Object> doubles = (ArrayList<Object>) o;
            double[] target = new double[doubles.size()];
            for (int i = 0; i < target.length; i++) {
                try {
//					 target[i] = doubles.get(i);                // java 1.5+ style (outboxing)
                    target[i] = getAsDoubleOrValue(doubles.get(i), 0);
                } catch (ClassCastException e) {
                    System.err.println(">>> erro convertendo valor para double:" + doubles.get(i).getClass().getName());
                    e.printStackTrace();
                }
            }
            d = target;
        } else {
            System.err.println(">>> ++ tipo do objeto nao pode ser convertido para double[]:" + o.getClass().getName());
        }

        return d;
    }

    public static double getAsDoubleOrValue(Object obj, double alternativo) {
        double ret = 0;
        if (obj == null) return alternativo;
        try {
            if (obj.getClass() == (new BigDecimal("0.0")).getClass()) {
                return ((BigDecimal) obj).doubleValue();
            } else if (obj.getClass() == (Integer.valueOf(0)).getClass()) {
                return ((Integer) obj).doubleValue();
            } else if (obj.getClass() == (Long.valueOf(0)).getClass()) {
                return ((Long) obj).doubleValue();
            } else if (obj.getClass() == (Float.valueOf(0)).getClass()) {
                return ((Float) obj).doubleValue();
            } else if (obj.getClass() == (Double.valueOf(0)).getClass()) {
                return ((Double) obj).doubleValue();
            } else if (obj.getClass() == (new String()).getClass()) {
                String valor = (String) obj;
                try {
                    return Double.valueOf(valor).doubleValue();
                } catch (NumberFormatException e) {
                    // se nao esta no formato, desformata
                    return stringToBigDecimal(valor).doubleValue();
                }
            } else if (obj.getClass().isArray() || obj.getClass() == ArrayList.class) {
                double[] aux = getAsDoubleArray(obj);
                if (aux.length > 0) {
//					>>> retornando primeiro elemento do array
                    return aux[0];
                }

            } else {
                System.err.println(">>> tipo do objeto nao pode ser convertido para double:" + obj.getClass().getName() + " : " + obj);
            }
        } catch (Exception e) {
            System.err.println(">>> erro na conversao do objeto para double:" + obj.getClass().getName());
            e.printStackTrace();
        }

        return ret;
    }

    public static BigDecimal stringToBigDecimal(String n) {
        return stringToBigDecimal(n, null);
    }

    public static BigDecimal stringToBigDecimal(String n, Locale locale) {
        if (locale == null) {
            locale = new Locale("pt", "BR");
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        BigDecimal ret = new BigDecimal(0);
        try {
            ret = new BigDecimal(numberFormat.parse(n).toString());
        } catch (ParseException e) {
//			e.printStackTrace();
        }
        return ret;
    }


    @SuppressWarnings("unchecked")
    public static int[] getAsIntArray(Object o) {
        int[] d = {};
        if (o.getClass().isArray()) {
            if (o.getClass().getComponentType().getName().equalsIgnoreCase("double")) {
                d = Arrays.stream((double[]) o).mapToInt(i -> Double.valueOf(i).intValue()).toArray();
            } else if (o.getClass().getComponentType().getName().equalsIgnoreCase("int")) {
                d = Arrays.stream((int[]) o).toArray(); // (int[])o ;
            } else if (o.getClass().getComponentType().getName().equalsIgnoreCase("java.math.BigDecimal")) {
                d = Arrays.stream((BigDecimal[]) o).mapToInt(Number::intValue).toArray();
                //((List<BigDecimal>)o).stream().mapToDouble(i-> i.doubleValue()).toArray();
            } else if (o.getClass().getComponentType().getName().equalsIgnoreCase("java.lang.String")) {
                d = Arrays.stream((String[]) o).mapToInt(Integer::valueOf).toArray();
                //((List<BigDecimal>)o).stream().mapToDouble(i-> i.doubleValue()).toArray();
            } else {
                System.err.println(">>> tipo da lista nao pode ser convertido para int[]:" + o.getClass().getName());
            }
        } else if (o.getClass() == ArrayList.class) {
            List<Object> ints = (ArrayList<Object>) o;
            int[] target = new int[ints.size()];
            for (int i = 0; i < target.length; i++) {
                try {
//					 target[i] = doubles.get(i);                // java 1.5+ style (outboxing)
                    target[i] = getAsIntOrValue(ints.get(i), 0);
                } catch (ClassCastException e) {
                    System.err.println(">>> erro convertendo valor para int:" + ints.get(i).getClass().getName());
                    e.printStackTrace();
                }
            }
            d = target;
        } else {
            int aux = getAsIntOrValue(o, 0);
            d = new int[]{aux};
        }

        return d;
    }

    public static int getAsIntOrValue(Object obj, int alternativo) {
        int ret = 0;
        if (obj == null) return alternativo;
        try {
            if (obj.getClass() == (new BigDecimal("0.0")).getClass()) {
                return ((BigDecimal) obj).intValue();
            } else if (obj.getClass() == (Integer.valueOf(0)).getClass()) {
                return ((Integer) obj).intValue();
            } else if (obj.getClass() == (Long.valueOf(0)).getClass()) {
                return ((Long) obj).intValue();
            } else if (obj.getClass() == (Float.valueOf(0)).getClass()) {
                return ((Float) obj).intValue();
            } else if (obj.getClass() == (Double.valueOf(0)).getClass()) {
                return ((Double) obj).intValue();
            } else if (obj.getClass() == ("").getClass()) {
                String valor = (String) obj;
                try {
                    return Integer.parseInt(valor);
                } catch (NumberFormatException e) {
                    // se nao esta no formato, desformata
                    return stringToBigDecimal(valor).intValue();
                }
            } else if (obj.getClass().isArray() || obj.getClass() == ArrayList.class) {
                int[] aux = getAsIntArray(obj);
                if (aux.length > 0) {
                    return aux[0];
                }

            } else {
                System.err.println(">>> tipo do objeto nao pode ser convertido para double:" + obj.getClass().getName() + " : " + obj);
            }
        } catch (Exception e) {
            System.err.println(">>> erro na conversao do objeto para double:" + obj.getClass().getName());
            e.printStackTrace();
        }

        return ret;
    }

}