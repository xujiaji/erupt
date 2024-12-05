package xyz.erupt.core.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author YuePeng
 * date 2021/3/28 17:45
 */
@Slf4j
public class EruptInformation {

    private static Properties props;

    static {
        String path = "/erupt-core.properties";
        Properties props = new Properties();
        try (InputStream stream = EruptInformation.class.getResourceAsStream(path)) {
            props.load(stream);
            EruptInformation.props = props;
        } catch (IOException e) {
            log.warn("erupt-core.properties load error", e);
        }
    }

    public static String getEruptVersion() {
        return props.getProperty("version");
    }


}
