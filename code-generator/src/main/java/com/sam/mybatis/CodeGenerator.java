package com.sam.mybatis;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author: DELL
 * @Date: 2020/12/11 13:25
 * @Description:
 */
public class CodeGenerator {

    public static String author="sam";

    private static String outputDir =System.getProperty("user.dir");

    public static String javaPath="/src/main/java";

    public static String resourcePath="/src/main/resources";

    public static String projectName="";
    public static String moduleName="";
    //包名
    private static String packageName="";

    private static String tableName="";
    private static String tablePrefix="";

    //数据配置
    private static String url="jdbc:mysql://192.168.1.22:3306/test?useUnicode=true&useSSL=false&characterEncoding=utf8";
    private static String user="root";
    private static String pwd="root";
    private static String driver="com.mysql.cj.jdbc.Driver";


    private static String CONTROLLER_TEMPLATE_PATH="templates/freemarker/controller.java.vm";
    private static String XML_TEMPLATE_PATH="templates/freemarker/mapper.xml.vm";

    /**
     * 初始化参数
     */
    private static void init(){
        //不要加后缀
        ResourceBundle rb = ResourceBundle.getBundle("mybatiesplus-config");
        author = rb.getString("author");

        //数据库
        user = rb.getString("jdbc.user");
        pwd = rb.getString("jdbc.pwd");
        url = rb.getString("jdbc.url");
        driver = rb.getString("jdbc.driver");

        //projectPath
        outputDir = rb.getString("outputDir");

        projectName = rb.getString("projectName");
        moduleName = rb.getString("moduleName");
        packageName = rb.getString("packageName");
        tableName = rb.getString("tableName");
        tablePrefix = rb.getString("tablePrefix");
    }

    public static void main(String[] args) {
        init();
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(globalConfig());
        mpg.setDataSource(dataSourceConfig());
        mpg.setStrategy(strategyConfig());

        mpg.setTemplate(templateConfig());
        mpg.setPackageInfo(packageConfig());
        mpg.setCfg(injectionConfig());
        // Velocity模板引擎解析
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();
    }

    /**
     * 全局配置
     * @return
     */
    public static GlobalConfig globalConfig(){
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(outputDir + "/" + projectName + "/" + javaPath);
        gc.setAuthor(author)
        .setOpen(true)
                .setServiceName("%sService")
                .setServiceImplName("%sServiceImpl")
        //实体属性 Swagger2 注解
        .setSwagger2(true)
        // XML ResultMap: mapper.xml生成查询映射结果
        .setBaseResultMap(true)
        // XML ColumnList: mapper.xml生成查询结果列
        .setBaseColumnList(true);
        return gc;
    }

    /**
     * 数据源配置
     * @return
     */
    public static DataSourceConfig dataSourceConfig(){
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(url);
        // dsc.setSchemaName("public");
        dataSourceConfig.setDriverName(driver);
        dataSourceConfig.setUsername(user);
        dataSourceConfig.setPassword(pwd);
        return dataSourceConfig;
    }

    /**
     * 策略配置
     * @return
     */
    public static StrategyConfig strategyConfig(){
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        //strategy.setSuperEntityColumns("id");
        if (StringUtils.isNotBlank(tableName)){
            strategy.setInclude(tableName.split(","));
        }
        strategy.setControllerMappingHyphenStyle(true);
        /*if (StringUtils.isNotBlank(tablePrefix)){
            strategy.setTablePrefix(tablePrefix.split(","));
        }*/
        // 生成实体类字段注解
        strategy.setEntityTableFieldAnnotationEnable(true);
        return strategy;
    }

    /**
     * 配置模板
     * @return
     */
    private static TemplateConfig templateConfig(){
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        templateConfig.setService("templates/service.java.vm");
        templateConfig.setController(CONTROLLER_TEMPLATE_PATH);
        templateConfig.setXml(XML_TEMPLATE_PATH);
        return templateConfig;
    }

    /**
     * 包配置
     * @return
     */
    private static PackageConfig packageConfig(){
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(moduleName);
        pc.setParent(packageName);
        return pc;
    }

    /**
     * 自定义配置
     * @return
     */
    private static InjectionConfig injectionConfig(){
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        cfg.setFileOutConfigList(fileOutConfigList());
        return cfg;
    }

    /**
     * 自定义输出配置
     * @return
     */
    private static List<FileOutConfig> fileOutConfigList() {
        List<FileOutConfig> focList = new ArrayList<>();

        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(XML_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                StringBuffer sb = new StringBuffer(outputDir).append("/");
                sb.append(projectName).append("/");
                sb.append(resourcePath).append("/");
                sb.append("mapper").append("/");
                sb.append(tableInfo.getEntityName()).append("Mapper");
                sb.append(StringPool.DOT_XML);
                return sb.toString();
            }
        });

        // 自定义配置会被优先输出
        /*focList.add(new FileOutConfig(CONTROLLER_TEMPLATE_PATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名
                StringBuffer sb = new StringBuffer(outputDir).append("/");
                sb.append(projectName).append("/");
                sb.append(javaPath).append("/");
                sb.append(packagePath).append("/");
                if (StringUtils.isNotBlank(moduleName)){
                    sb.append(moduleName).append("/");
                }
                sb.append("controller").append("/");
                sb.append(tableInfo.getControllerName());
                sb.append(StringPool.DOT_JAVA);

                return sb.toString();
            }
        });*/
        return focList;
    }

}
