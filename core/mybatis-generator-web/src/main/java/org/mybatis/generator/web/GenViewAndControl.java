package org.mybatis.generator.web;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.mybatis.generator.api.GetTablesProgressCallback;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.config.TableConfiguration;
//import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.util.*;


/**
 * Created by changxin on 2016/8/14.
 * 生成表示层和控制层代码
 */
public class GenViewAndControl extends GetTablesProgressCallback {

    private static final String templateDir = "D:\\eclipseworkspace\\logomama\\trunk\\logomamaproject\\logomam-codegen\\src\\main\\resources\\template"; //输入目录
    private static final String outputDir = "d://aa//"; //输出目录
    private static final String tableName ="wiki_casetype_lawyer";//需要生成代码的表名，*表示所有
    private static Configuration configuration = new Configuration();//freemark 的配置对象


    static {
        File f = new File(outputDir);
        if (!f.exists()) {
            f.mkdir();
        }

        //创建一个合适的Configration对象
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateDir));
            configuration.setObjectWrapper(new DefaultObjectWrapper());
            configuration.setDefaultEncoding("UTF-8");   //这个一定要设置，不然在生成的页面中 会乱码
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startTask(String taskName) {
        //System.out.println(taskName);
    }


    /**
     *
     * IntrospectedTable table
     * table.getFullyQualifiedTableNameAtRuntime() 是表名
     * table.getFullyQualifiedTable().getDomainObjectName() 是Model名，也是表名去掉下划线，pascal命名法。
     * table.getAllColumns() 取得列名对象集合
     *
     * IntrospectedColumn col
     * col.getActualColumnName() 列名
     * col.getJavaProperty()  列对应的java属性名，去掉下划线，对应骆驼命名法。
     * col.getJdbcTypeName()  列类型且大写，如INTEGER，BIGINT，VARCHAR
     * col.getRemarks()  列注释
     * col.getLength()  字段长度
     *
     * 文件命名规则：
     *      总原则，会暴露在url路径中的全部小写
     *      html  全部小写表名.html
     *      js Controller   全部小写表名ctrl.js
     *      java Controller pascal帕斯卡命名   AaBbController.java
     * 类命名：
     *      js Controller  类命名和java 类命名都是 帕斯卡
     *
     *
     * @param introspectedTables
     * @param tableConfigurations
     */
    @Override
    public void genFromTemplate(List<IntrospectedTable> introspectedTables, ArrayList<TableConfiguration> tableConfigurations) {

        for (IntrospectedTable table : introspectedTables) {
           // System.out.println("|--" +  tableConfigurations.get(0).getMapperName());
            String pascalTableName= table.getFullyQualifiedTable().getDomainObjectName();
//            for (IntrospectedColumn col : table.getAllColumns()) {
//                System.out.println("|--" +  col.getActualColumnName());
//            }

            if( tableName.equals("*")){
                System.out.println("针对>>>"+table.getFullyQualifiedTableNameAtRuntime());
                output(table.getFullyQualifiedTable().getDomainObjectName(), table.getAllColumns(),pascalTableName);

            }else if( tableName.equals(table.getFullyQualifiedTableNameAtRuntime()) ){
                System.out.println("针对>>>"+tableName+"开始所生成");
                output(table.getFullyQualifiedTable().getDomainObjectName(), table.getAllColumns(),pascalTableName);
            }
        }
    }

    /**
     * 输出,写到硬盘
     */
    private void output(String DomainObjectName, List<IntrospectedColumn> DomainObjectPropertys,String pascalTableName)   {

        try {
            String tablePascalName=DomainObjectName;
            String jsCtrlFileName= tablePascalName.toLowerCase()+"ctrl.js";//js ctrl 的文件名
            String mapperName= tablePascalName+"Mapper";//mybatis generator 中mapper 命名
            String exampleName=tablePascalName+"Example";//mybatis generator 中 Example 查询条件工具类命名
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("DomainObjectName", DomainObjectName);
            paramMap.put("DomainObjectPropertys", DomainObjectPropertys);
            paramMap.put("jsCtrlClassName", tablePascalName+"Ctrl" );//js control 类命名
            paramMap.put("jsCtrlFileName", jsCtrlFileName ); //js control 文件命名
            paramMap.put("createDate", new Date());
            paramMap.put( "MapperName", mapperName);
            paramMap.put( "ExampleName", exampleName);
           // paramMap.put( "tablePascalName", tablePascalName);




            //<editor-fold desc="生成angularjs中的Ctrl">
            //StringUtils.uncapitalize(pascalTableName )
            Template angularjsCtrlTemplate = configuration.getTemplate("angularjsctrl.js");//模板
            Writer angularjsCtrlWriter = new OutputStreamWriter(new FileOutputStream(outputDir + jsCtrlFileName), "UTF-8");//输出
            angularjsCtrlTemplate.process( paramMap, angularjsCtrlWriter );
            angularjsCtrlWriter.close();
            //</editor-fold>


            //<editor-fold desc="生成angularjs中的html">
            String lowerAngularjsHtmlName = pascalTableName.toLowerCase() +".html"; //文件名，全小写
            Template angularjsHtmlTemplate = configuration.getTemplate("angularjsHtml.html");//模板
            Writer angularjsHtmlWriter = new OutputStreamWriter(new FileOutputStream(outputDir + lowerAngularjsHtmlName), "UTF-8");//输出
            angularjsHtmlTemplate.process( paramMap, angularjsHtmlWriter );
            angularjsHtmlWriter.close();
            //</editor-fold>

            //<editor-fold desc="生成Controller 整合Spirng REST中的java">
            String springRESTControllerName=pascalTableName +"Controller.java"; //文件名
            Template springRESTControllerTemplate = configuration.getTemplate("SpringRESTController.java");//模板
            Writer springRESTControllerWriter = new OutputStreamWriter(new FileOutputStream(outputDir + springRESTControllerName), "UTF-8");//输出
            springRESTControllerTemplate.process( paramMap, springRESTControllerWriter );
            springRESTControllerWriter.close();
            //</editor-fold>

            //<editor-fold desc="生成临时文件 配置片段">
            Template tempConfigTemplate = configuration.getTemplate("tempConfig.js");//模板
            Writer tempConfigWriter = new OutputStreamWriter(new FileOutputStream(outputDir + "tempConfig.js"), "UTF-8");//输出
            tempConfigTemplate.process( paramMap, tempConfigWriter );
            tempConfigWriter.close();
            //</editor-fold>

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
