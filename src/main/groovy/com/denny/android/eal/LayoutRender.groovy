package com.denny.android.eal

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.swing.LookAndFeel

/**
 * Created by hasee on 2017/5/23.
 */
class LayoutRender extends DefaultTask{

    String resPath;
    File sourceLayoutDir;

    @TaskAction
    def processRender(){
        logger.info("Render")
        logger.warn(resPath)
        logger.warn("layout:${getLayoutPath()}")
//        File test = new File(getLayoutPath(),"test.xml")
//        logger.warn(test.absolutePath)
        sourceLayoutDir.listFiles().each {
            if(needRender(it)){
                logger.warn("${it.name} need render")
                renderContent(it)
            }
        }
    }

    def needRender(File f){
        new XmlParser().parse(f).name()=='layout'
    }

    def renderContent(content){
        Node xml = new XmlParser().parse(content)
        String cetAttr = xml.attributes()['content']

        def cet = xml.depthFirst().find {
            it.name()=='content'
        }
        def values = cet.children()
        logger.warn("${values}")



        def layoutId =  cetAttr.split('/')[1]+".xml"
        def layoutXml = new XmlParser().parse(findLayout(layoutId))
        def lc = layoutXml.depthFirst().find {
            it.name()=='layoutContent'
        }
        def parent = lc.parent()
        parent.remove(lc)
        values.each{
            parent.append(it)
        }
        println()
        logger.warn("${parent}")

        def writer = new FileWriter(getOutputFile(content))
        def printer = new XmlNodePrinter(new PrintWriter(writer))
        printer.preserveWhitespace = true
        printer.print(layoutXml)
    }

    def getOutputFile(File f){
        FileUtils.getFile(resPath, "layout",f.name)
    }

    def findLayout(name){
        FileUtils.getFile(resPath, "layout",name)
    }

    def getLayoutPath(){
        FileUtils.getFile(resPath,"layout").absolutePath;
   }

    def listLayouts(Closure c){
        sourceLayoutDir.listFiles().each(c)
    }
}
