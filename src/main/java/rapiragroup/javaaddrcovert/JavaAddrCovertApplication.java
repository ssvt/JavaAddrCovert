package rapiragroup.javaaddrcovert;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rapiragroup.javaaddrcovert.services.ConfigService;
import rapiragroup.javaaddrcovert.services.Decoder_Legacy;
import rapiragroup.javaaddrcovert.services.Decoder_SegWit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Sergey Svyatoha
 * @version 1.0
 *
 *     Legacy (P2PKH): начинается с цифры 1. Пример: 1N4Qbzg6LSXUXyXu2MDuGfzxwMA7do8AyL.                    (BIP32; BIP44)
 *     Script (P2SH): начинается с цифры 3. Пример: 3J98t1WpEZ73CNmQviecrnyiWrnqRhWNLy.                     (BIP49)
 *     SegWit (P2WPKH): начинается с комбинации “bc1q”. Пример: bc1qfg9t7fwn0atn4yf9spca5502vk8dyhq8a9aqd8. (BIP84)
 *     Taproot (P2TR): начинается с комбинации “bc1p”. Пример: bc1peu5hzzyj8cnqm05le6ag7uwry0ysmtf3v4uuxv3v8hqhvsatca8ss2vuwx.
 */

@SpringBootApplication
@Log4j2
public class JavaAddrCovertApplication implements CommandLineRunner {
    private static String ORIGIN_ADDR = "D:\\COMMON\\TEMP\\tables_origin\\all_addresses.txt";
    private Map<String, TreeSet<String>> mapLegacy = new HashMap<>();
    private Map<String, TreeSet<String>> mapScrypt = new HashMap<>();
    private Map<String, TreeSet<String>> mapSegWit = new HashMap<>();

    @Autowired
    ConfigService configService;
    public static void main(String[] args) {
        SpringApplication.run(JavaAddrCovertApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(ORIGIN_ADDR))) {
            String line;
            Integer count = 0;
            while ((line = br.readLine()) != null){

                if (line.startsWith("1")) {
                    var legacyAddr = Decoder_Legacy.decodeJ(line);
                    mapLegacy.computeIfAbsent(legacyAddr.substring(0, 2), k-> new TreeSet<>()).add(legacyAddr);
                }else if (line.startsWith("3")) {
                    var scryptAddr = Decoder_Legacy.decodeJ(line);
                    mapScrypt.computeIfAbsent(scryptAddr.substring(0, 2), k-> new TreeSet<>()).add(scryptAddr);
                }else if (line.startsWith("bc1q")) {
                    if (line.length() > 42){
                        count++;
                        continue;
                    }
                    var segwitAddr = Decoder_SegWit.decodeJ(line);
                    mapSegWit.computeIfAbsent(segwitAddr.substring(0, 2), k-> new TreeSet<>()).add(segwitAddr);
                }else {

                }

                if (count % 500000 == 0) {
                    System.out.print(String.format("\rreading: %d", count));
                }
                count++;

            }

            System.out.println(String.format("\nПакет_10млн -> Общее число строк: %,d \nLegacy (P2PKH): %d | Script (P2SH): %d | SegWit (P2WPKH): %d",
                    count,
                    mapLegacy.values().stream().mapToInt(TreeSet::size).sum(),
                    mapScrypt.values().stream().mapToInt(TreeSet::size).sum(),
                    mapSegWit.values().stream().mapToInt(TreeSet::size).sum()));
            writeMapToFiles();

        }catch (IOException e){
            log.error(e);
        }

    }

    private void writeMapToFiles() {
        System.out.println("\nЗапись данных Legacy.....");
        for (Map.Entry<String, TreeSet<String>> entry : mapLegacy.entrySet()) {
            String fileName = entry.getKey() + ".csv";
            TreeSet<String> lines = entry.getValue();
            File file = Paths.get(ConfigService.PATH_LEGACY, fileName).toFile();
            try {
                // Запись строк в файл
                FileUtils.writeLines(file, lines, true);
                System.out.print(String.format("\rзапись данных в файл: %s", fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nЗапись данных Scrypt.....");
        for (Map.Entry<String, TreeSet<String>> entry : mapScrypt.entrySet()) {
            String fileName = entry.getKey() + ".csv";
            TreeSet<String> lines = entry.getValue();
            File file = Paths.get(ConfigService.PATH_NATIVE_SEGWIT, fileName).toFile();
            try {
                // Запись строк в файл
                FileUtils.writeLines(file, lines, true);
                System.out.print(String.format("\rзапись данных в файл: %s", fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nЗапись данных Segwit.....");
        for (Map.Entry<String, TreeSet<String>> entry : mapSegWit.entrySet()) {
            String fileName = entry.getKey() + ".csv";
            TreeSet<String> lines = entry.getValue();
            File file = Paths.get(ConfigService.PATH_SEGWIT, fileName).toFile();
            try {
                // Запись строк в файл
                FileUtils.writeLines(file, lines, true);
                System.out.print(String.format("\rзапись данных в файл: %s", fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
