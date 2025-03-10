package com.api.poc_jdbc.type;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import com.api.poc_jdbc.utils.DateUtils;
import com.api.poc_jdbc.utils.RegistroUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Registro extends LinkedHashMap<String, Object>{

    private static final long serialVersionUID = -9100647403066998159L;

    public String getAsString(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            return String.valueOf(this.get(key));
        }
        return null;
    }

    public String getAsStringOrValue(String key, String value) {
        String ret = getAsString(key);
        return ( ret == null || StringUtils.isBlank(ret) ? value : ret );
    }

    public String getAsStringNN(String key) {
        String ret = getAsString(key);
        return ( ret == null ? "" : ret );
    }

    public double getAsDouble(String key) {
        if ( this.containsKey(key) && this.get(key) != null) {
            Object obj = this.get(key);
            try {
                if ( obj.getClass() == (new BigDecimal("0.0")).getClass() ) {
                    return ((BigDecimal)obj).doubleValue();
                } else if ( obj.getClass() == (Integer.valueOf(0)).getClass() ) {
                    return ((Integer)obj).doubleValue();
                } else if ( obj.getClass() == (Long.valueOf(0)).getClass() ) {
                    return ((Long)obj).doubleValue();
                } else if ( obj.getClass() == (Float.valueOf(0)).getClass() ) {
                    return ((Float)obj).doubleValue();
                } else if ( obj.getClass() == (Double.valueOf(0)).getClass() ) {
                    return ((Double)obj).doubleValue();
                } else if ( obj.getClass() == (new String()).getClass() ) {
                    String valor = (String)obj ;
                    try {
                        return Double.valueOf(valor).doubleValue();
                    } catch (NumberFormatException e) {
                        // se nao esta no formato, desformata
                        return stringToBigDecimal(valor).doubleValue();
                    }
                } else {
                    System.err.println(">>> tipo do objeto nao pode ser convertido para double:" + obj.getClass().getName() + " : " + obj);
                }
            } catch (Exception e) {
                System.err.println(">>> erro na conversao do objeto para double:" + obj.getClass().getName());
                e.printStackTrace();
            }
        }
        return 0.0d;
    }

    public BigDecimal getAsBigDecimal(String key) {
        if ( this.containsKey(key)) {
            Object obj = this.get(key);
            try {
                if ( obj.getClass() == (new BigDecimal("0.0")).getClass() ) {
                    return (BigDecimal)obj;
                } else if ( obj.getClass() == (Integer.valueOf(0)).getClass() ) {
                    return new BigDecimal((Integer)obj);
                } else if ( obj.getClass() == (Long.valueOf(0)).getClass() ) {
                    return new BigDecimal((Long)obj);
                } else if ( obj.getClass() == (Float.valueOf(0)).getClass() ) {
                    return new BigDecimal((Float)obj);
                } else if ( obj.getClass() == (Double.valueOf(0)).getClass() ) {
                    return new BigDecimal((Double)obj);
                } else if ( obj.getClass() == (new String()).getClass() ) {
                    String valor = (String)obj ;
                    try {
                        return new BigDecimal(valor);
                    } catch (NumberFormatException e) {
                        // se nao esta no formato, desformata
                        return stringToBigDecimal(valor);
                    }
                } else {
                    System.err.println(">>> tipo do objeto nao pode ser convertido para BigDecimal:" + obj.getClass().getName() + " : " + obj);
                }
            } catch (Exception e) {
                System.err.println(">>> erro na conversao do objeto para BigDecimal:" + obj.getClass().getName());
                e.printStackTrace();
            }
        }
        return null;
    }

    public BigDecimal getAsBigDecimalOrValue(String key, BigDecimal defaultValue) {
        Object value = this.get(key);

        if (value == null) {
            return defaultValue;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue; // Retorna o valor padrão caso haja erro na conversão
        }
    }

    public BigDecimal getStringAsBigDecimal(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            String obj = this.getAsString(key);
            return stringToBigDecimal(obj);
        }
        return null;
    }

    public Registro getAsRegistro(String key) {
        return (Registro) this.getOrDefault(key, null);
    }

    @SuppressWarnings("unchecked")
    public List<Registro> getAsList(String key) {
        return (List<Registro>) this.getOrDefault(key, null);
    }

    public Integer getAsIntOrZero(String key) {
        return getAsIntOrValue(key, 0);
    }

    public Integer getAsIntOrValue(String key, int value) {
        Integer ret = getAsInt(key);
        return ( ret == null ? value : ret );
    }


    public Integer getAsInt(String key) {
        try {
            Object obj = this.get(key);
            if ( obj != null) {
                if ( obj.getClass() == (new BigDecimal("0.0")).getClass() ) {
                    return ((BigDecimal)obj).intValue();
                } else if ( obj.getClass() == (Integer.valueOf(0)).getClass() ) {
                    return (Integer)obj;
                } else if ( obj.getClass() == (Long.valueOf(0)).getClass() ) {
                    return ((Long)obj).intValue();
                } else if ( obj.getClass() == (Float.valueOf(0)).getClass() ) {
                    return ((Float)obj).intValue();
                } else if ( obj.getClass() == (Double.valueOf(0)).getClass() ) {
                    return ((Double)obj).intValue();
                } else if ( obj.getClass() == ("").getClass() ) {
                    if ( StringUtils.isBlank((String)obj) )
                        return null;
                    return Integer.valueOf((String)obj);
                } else {
                    System.err.println(">>> tipo do objeto nao pode ser convertido para Integer:" + obj.getClass().getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getAsLong(String key) {
        if ( this.containsKey(key)) {
            Object obj = this.get(key);
            if ( obj.getClass() == (new BigDecimal("0.0")).getClass() ) {
                return ((BigDecimal)obj).longValue();
            } else if ( obj.getClass() == (Integer.valueOf(0)).getClass() ) {
                return ((Integer)obj).longValue();
            } else if ( obj.getClass() == (Long.valueOf(0)).getClass() ) {
                return (Long)obj;
            } else if ( obj.getClass() == (Float.valueOf(0)).getClass() ) {
                return ((Float)obj).longValue();
            } else if ( obj.getClass() == (Double.valueOf(0)).getClass() ) {
                return ((Double)obj).longValue();
            } else if ( obj.getClass() == (new String()).getClass() ) {
                if ( StringUtils.isBlank((String)obj) )
                    return null;
                return Long.valueOf((String)obj);
            } else {
                System.err.println(">>> tipo do objeto nao pode ser convertido para Long:" + obj.getClass().getName());
            }
        }
        return null;
    }

    public Date getAsDate(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            Object obj = this.get(key);
            if ( obj.getClass() == (new Date()).getClass() ) {
                return (Date)obj;
            } else if ( obj.getClass() == java.sql.Timestamp.class ) {
                return new Date(((java.sql.Timestamp)obj).getTime());
            } else if ( obj.getClass() == java.sql.Date.class ) {
                return new Date(((java.sql.Date)obj).getTime());
            }  else {
                System.err.println(">>> tipo do objeto nao pode ser convertido para Date:" + obj.getClass().getName());
            }
        }
        return null;
    }

    public Timestamp getAsTimestamp(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            Object obj = this.get(key);
            if ( obj.getClass() == java.sql.Timestamp.class ) {
                return (Timestamp) obj;
            } else if ( obj.getClass() == java.sql.Date.class ) {
                return new Timestamp(((java.sql.Date)obj).getTime());
            } else {
                System.err.println(">>> tipo do objeto nao pode ser convertido para Timestamp:" + obj.getClass().getName());
            }
        }
        return null;
    }


    private static BigDecimal stringToBigDecimal(String n) {
        return stringToBigDecimal(n, null);
    }

    private static BigDecimal stringToBigDecimal(String n, Locale locale){
        if ( locale == null ) {
            locale = new Locale("pt", "BR");
        }
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        BigDecimal ret = new BigDecimal(0);
        try {
            ret = new BigDecimal( numberFormat.parse(n).toString() );
        } catch (ParseException e) {
//			System.err.println(String.format("Erro convertendo numero: [%s]", n));
//			e.printStackTrace();
        }
        return ret ;
    }

    public static Registro build() {
        return new Registro();
    }

    public Registro with(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public String getAsJSON() {
        String ret = "";
        try {
            ret = new ObjectMapper().writer().writeValueAsString(this) ;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public boolean getAsBoolean(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            try {
                Object obj = this.get(key);
                if (obj.getClass() == String.class)
                    return Boolean.valueOf(((String) this.get(key)).trim().toLowerCase());
                else if (obj.getClass() == Boolean.class)
                    return Boolean.valueOf((Boolean)this.get(key));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Boolean.FALSE;
    }

    public boolean getAsBooleanOrValue(String key, boolean value){
        if (this.containsKey(key) && this.get(key) != null) {
            try {
                Object obj = this.get(key);
                return getAsBoolean(key);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public List<String> getFields() {
        return new ArrayList<String>(this.keySet());
    }

    public byte[] getAsByte(String key) {
        if (this.containsKey(key) && this.get(key) != null) {
            return (byte[]) this.get(key);
        }
        return null;
    }

    public String getArrayAsString( String key ) {
        String ret = "";
        Object obj = this.get(key);
        if ( obj != null ) {
            if ( obj.getClass() == ArrayList.class || obj.getClass() == List.class )  {
                ret = (((ArrayList<?>)obj).toString()).replaceAll("\\[|\\]", "");
            } else if ( obj.getClass() == (new String()).getClass() || obj.getClass() == Integer.valueOf(0).getClass() ) {
                ret = this.getAsString(key);
            }
        }
        return ret;
    }

    public String getArrayAsStringOrValue( String key , String value ) {
        String ret = getArrayAsString(key);
        return ( ret == null ? value : ret );
    }

    @SuppressWarnings("unchecked")
    public List<BigDecimal> getAsListBigDecimal(String key) {
        return (List<BigDecimal>) this.getOrDefault(key, null);
    }

    @SuppressWarnings("unchecked")
    public List<String> getAsListString(String key) {
        return (List<String>) this.getOrDefault(key, null);
    }

    public Date getStringAsDate(String key, String format) {
        return getStringAsDateOrValue(key, format, null);
    }

    public Date getStringAsDateOrValue(String key, String format, Date value) {
        if (this.containsKey(key) && this.get(key) != null) {
            String obj = this.getAsStringNN(key);
            if ( obj.isEmpty() ) return value;
            return DateUtils.textoParaData(obj, format);
        }
        return value;
    }

    public int[] getAsIntArray(String key) {
        int[] ret = {};
        if ( this.containsKey(key)) {
            Object o = this.get(key);
            ret = RegistroUtils.getAsIntArray(o);
//			Object obj = this.get(key);
//			if ( obj != null ) {
//				if ( obj.getClass().isArray() ) {
//					if ("[I".equalsIgnoreCase( obj.getClass().getName() ) ) {
//						ret = (int[])obj ;
//					} else if ( "[Ljava.lang.String;".equalsIgnoreCase( obj.getClass().getName() ) ) {
//						String[] aux = (String[])obj;
//						ret = new int[ aux.length ];
//					}
//				} else if ( obj.getClass() == org.json.JSONArray.class ) {
//					JSONArray ja = (JSONArray) obj;
//					ret = new int[ ja.length() ];
//					for( int i = 0 ; i < ja.length() ; i++ ) {
//						ret[i] = ja.optInt(i);
//					}
//				}
//			}
        }
        return ret;
    }

    public double[] getAsDoubleArray(String key) {
        double [] d = {};
        if ( this.containsKey(key) ) {
            Object o = this.get(key);
            d = RegistroUtils.getAsDoubleArray(o);
        }
        return d;
    }

    public BigDecimal getAsBigDecimalOrValue(String key, double defaultValue) {
        return getAsBigDecimalOrValue(key, new BigDecimal(defaultValue));
    }
}
