package rapiragroup.javaaddrcovert.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import rapiragroup.javaaddrcovert.models.MyConfig;

import java.io.File;
import java.io.IOException;



@Service
@Log4j2
public class ConfigService {

    private final String configFile = "D:\\COMMON\\TEMP\\config.cfg";
    public static String PATH_LEGACY;
    public static String PATH_SEGWIT;
    public static String PATH_NATIVE_SEGWIT;

    private Gson gson = new GsonBuilder().create();

    @PostConstruct
    public void readConfig(){
        log.info("Reading config file");
        try {
            String lineCfg = FileUtils.readFileToString(new File(configFile), "UTF-8");
            MyConfig config = gson.fromJson(lineCfg, MyConfig.class);

            PATH_LEGACY = config.getFolder_tables_legacy();
            PATH_SEGWIT = config.getFolder_tables_segwit();
            PATH_NATIVE_SEGWIT = config.getFolder_tables_native_segwit();

        } catch (IOException e) {
            log.error(e);
        }
    }
}
