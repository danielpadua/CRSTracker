package br.com.danielpadua.crstracker.util;

import java.util.Locale;

/**
 * Created by danielpadua on 14/02/2018.
 */

public class CRSUtil {

    ///////////////////////////////////
    // Globals ////////////////////////
    ///////////////////////////////////
    public static final int GLOBAL_DEFAULT_TIMEOUT_SECONDS = 10;
    public static final int GLOBAL_SOUTHXCHANGE_API_CALL_TIMER = 10; // Seconds
    public static final int GLOBAL_CFINEX_API_CALL_TIMER = 10; // Seconds
    public static final int GLOBAL_CREX24_API_CALL_TIMER = 10; // Seconds
    public static final int GLOBAL_FXCBIT_API_CALL_TIMER = 10; // Seconds
    public static final int GLOBAL_BITVALOR_API_CALL_TIMER = 60; // Seconds
    public static final int GLOBAL_BINANCE_API_CALL_TIMER = 15; // Seconds
    public static final String GLOBAL_DEFAULT_24H_TICKER_DESCRIPTION = "24 horas";
    public static final String GLOBAL_DEFAULT_12H_TICKER_DESCRIPTION = "12 horas";
    public static final String GLOBAL_DEFAULT_1H_TICKER_DESCRIPTION = "1 hora";
    public static final String GLOBAL_CRS_SHARED_PREFERENCES_NAME = "CRS_SP";


    ///////////////////////////////////
    // Shared Preferences /////////////
    ///////////////////////////////////
    public static final String SHARED_PREFERENCES_SOUTHXCHANGE = "SOUTHXCHANGE";
    public static final String SHARED_PREFERENCES_CFINEX = "CFINEX";
    public static final String SHARED_PREFERENCES_CREX24 = "CREX24";
    public static final String SHARED_PREFERENCES_FXCBIT = "FXCBIT";


    ///////////////////////////////////
    // Widget /////////////////////////
    ///////////////////////////////////


    ///////////////////////////////////
    // Exceptions /////////////////////
    ///////////////////////////////////
    public static final String EXCEPTION_PATTERN_API_TIMEOUT = "{0} demorou mais de {1} segundos para responder o par: CRS/{2}. Tente novamente mais tarde";
    public static final String EXCEPTION_PATTERN_API_STATUS_500 = "{0} apresentou problemas internos ao responder o par: CRS/{1}";
    public static final String EXCEPTION_PATTERN_API_INVALID_JSON = "{0} apresentou resposta JSON inválida no par: CRS/{1}";
    public static final String EXCEPTION_PATTERN_API_NOT_TREATED = "Erro não tratado na exchange: {0}, ao responder o par: CRS/{1}. Tente novamente mais tarde";
    public static final String EXCEPTION_TICKER_LABEL = "erro";
    public static final String EXCEPTION_TICKER_DESCRIPTION = "erro";

    ///////////////////////////////////
    // JSON Parsers ///////////////////
    ///////////////////////////////////
    public static final String JSON_TICKER_24H_LABEL = "ticker24h";
    public static final String JSON_TICKER_12H_LABEL = "ticker12h";
    public static final String JSON_TICKER_1H_LABEL = "ticker1h";

    // Southxchange
    public static final String JSON_SOUTHXCHANGE_BID_LABEL = "Bid";
    public static final String JSON_SOUTHXCHANGE_ASK_LABEL = "Ask";
    public static final String JSON_SOUTHXCHANGE_LAST_LABEL = "Last";
    public static final String JSON_SOUTHXCHANGE_VARIATION_LABEL = "Variation24Hr";
    public static final String JSON_SOUTHXCHANGE_VOLUME_LABEL = "Volume24Hr";

    // CFinex
    public static final String JSON_CFINEX_MAINPAIR_LABEL = "MainPair";
    public static final String JSON_CFINEX_BID_LABEL = "Bid";
    public static final String JSON_CFINEX_ASK_LABEL = "Ask";
    public static final String JSON_CFINEX_PRICE_LABEL = "Price";
    public static final String JSON_CFINEX_PERCENT_LABEL = "Percent";
    public static final String JSON_CFINEX_LOCALVOLUME_LABEL = "LocalVolume";

    // Crex24
    public static final String JSON_CREX24_TICKERS_LABEL = "Tickers";
    public static final String JSON_CREX24_LAST_LABEL = "Last";
    public static final String JSON_CREX24_LOW_LABEL = "LowPrice";
    public static final String JSON_CREX24_HIGH_LABEL = "HighPrice";
    public static final String JSON_CREX24_PERCENT_CHANGE_LABEL = "PercentChange";
    public static final String JSON_CREX24_VOLUME_LABEL = "VolumeInBtc";

    // FXCBit
    public static final String JSON_FXCBIT_LAST_LABEL = "crsbrl";

    // Bitvalor
    public static final String JSON_BITVALOR_TICKER24H_LABEL = "ticker_24h";
    public static final String JSON_BITVALOR_TICKER12H_LABEL = "ticker_12h";
    public static final String JSON_BITVALOR_TICKER1H_LABEL = "ticker_1h";
    public static final String JSON_BITVALOR_TOTAL_LABEL = "total";
    public static final String JSON_BITVALOR_LAST_LABEL = "last";
    public static final String JSON_BITVALOR_HIGH_LABEL = "high";
    public static final String JSON_BITVALOR_LOW_LABEL = "low";
    public static final String JSON_BITVALOR_VOLUME_LABEL = "vol";
    public static final String JSON_BITVALOR_TRADE_COUNT_LABEL = "trades";

    // Binance
    public static final String JSON_BINANCE_PRICE_LABEL = "price";


    public static Double validateNullDoubleAPI(String apiOutput) {
        return apiOutput.equals("null") ? 0 : Double.parseDouble(apiOutput.replace(",", "."));
    }

    public static String formatCryptoPrice(Double price) {
        return String.format(Locale.ROOT, "%.8f", price).replace(",", ".");
    }

    public static String formatFiatPrice(Double price) {
        return String.format(Locale.ROOT, "%.2f", price).replace(",", ".");
    }

    public static String formatVolume(Double volume) {
        return String.format(Locale.ROOT, "%.8f", volume).replace(",", ".");
    }

    public static String formatVariation(Double variation) {
        return String.format(Locale.ROOT, "%.2f", variation).replace(",", ".") + "%";
    }
}
