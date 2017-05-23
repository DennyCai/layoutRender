package com.denny.android.eal

import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

class EalPlugins implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.logger.info("apply plugins");
        project.afterEvaluate {

            project.android.applicationVariants.all{ ApplicationVariant variant->
                def resDir
                project.logger.info("EalPlugins");
                variant.sourceSets.each {
                    if(it.name=='main'){
                        resDir = it.resDirectories.asList().first()
                    }

                }
                def variantOutput = variant.outputs.first()
                def path = variantOutput.processResources.resDir.getPath()

                LayoutRender renderTask = project.tasks.create("processRender${variant.name.capitalize()}",LayoutRender)
                renderTask.group = "layoutRender"
                renderTask.resPath = path;
                renderTask.sourceLayoutDir = new File(resDir,"layout")
//                renderTask.mustRunAfter variantOutput.processResources


                variantOutput.processResources.dependsOn renderTask
            }
        }
    }
}