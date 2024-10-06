package rapiragroup.javaaddrcovert.services;

import org.bitcoinj.base.Base58;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

/**
 * @author Sergey Svyatoha
 * декодирование Legacy (P2PKH) начинается с цифры 1
 * и Script (P2SH) начинаются с цыфры 3
 */

public class Decoder_Legacy {

    public static String decodeJ(String address){

        try {
            // Декодируем адрес из Base58Check
            byte[] decoded = Base58.decodeChecked(address);
            // Извлекаем хэш публичного ключа (последние 20 байтов)
            byte[] hash160 = Arrays.copyOfRange(decoded, 1, decoded.length);
            // Преобразуем хэш в hex-формат
            String hexHash = DatatypeConverter.printHexBinary(hash160).toLowerCase();

            return hexHash;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
