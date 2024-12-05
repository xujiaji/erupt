package xyz.erupt.monitor.vo;

import lombok.Getter;
import lombok.Setter;
import xyz.erupt.monitor.util.SystemUtil;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Properties;

/**
 * @author YuePeng
 * date 2021/1/23 23:27
 */
@Getter
@Setter
public class Jvm {

    private String name;

    private String vendor;

    private String version;

    private String inputArgs;

    private String pid;

    private String home;

    private String path;

    private int threadCount;

    //-------------------- 内存
    private String total;

    private String used;

    private String free;

    private String usage;


    Jvm() {
        Properties props = System.getProperties();
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        this.setTotal(SystemUtil.formatByte(total));
        this.setUsed(SystemUtil.formatByte(total - free));
        this.setFree(SystemUtil.formatByte(free));
        this.setUsage(new DecimalFormat("#.##%").format((total - free) * 1.0 / total));
        this.setVersion(props.getProperty("java.version"));
        this.setHome(props.getProperty("java.home"));
        this.setInputArgs(ManagementFactory.getRuntimeMXBean().getInputArguments().toString());
        this.setPid(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        this.setName(props.getProperty("java.vm.name"));
        this.setVendor(props.getProperty("java.vendor"));
        this.setPath(props.getProperty("user.dir"));
        this.setThreadCount(ManagementFactory.getThreadMXBean().getThreadCount());
    }

}
