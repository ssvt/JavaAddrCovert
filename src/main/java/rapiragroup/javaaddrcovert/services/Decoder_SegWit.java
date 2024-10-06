package rapiragroup.javaaddrcovert.services;

import org.bitcoinj.base.Address;
import org.bitcoinj.base.SegwitAddress;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;

import javax.xml.bind.DatatypeConverter;

/**
 * @author Sergey Svyatoha
 * декодирование SegWit (P2WPKH)
 * начинается с комбинации “bc1q”
 *  Bech32 состоит из двух частей:Человеко-читаемая часть (HRP) bc-> основная сеть и Данные (data), кодированные с использованием Base32.
 */
public class Decoder_SegWit {

    /** Декодирование Bech32 адреса
     * Bech32 состоит из двух частей:Человеко-читаемая часть (HRP) bc-> основная сеть и Данные (data), кодированные с использованием Base32.
     */

    public static String decodeJ (String address) {
        NetworkParameters params = MainNetParams.get();
        try {
            SegwitAddress addr = (SegwitAddress) Address.fromString(params, address);

            // Получаем хэш публичного ключа (Hash160)
            byte[] hash = addr.getHash();

            // Преобразуем хэш в hex-формат
            String hexHash = DatatypeConverter.printHexBinary(hash).toLowerCase();
            return hexHash;
        }catch (Exception ex) {
            throw new RuntimeException("Error while decoding segwit address", ex);
        }

    }
}
