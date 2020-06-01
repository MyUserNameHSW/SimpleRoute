package com.xiaoshijie.sqb.route.compiler.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.xiaoshijie.sqb.route.annotation.Route;
import com.xiaoshijie.sqb.route.compiler.utils.Consts;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * @author heshuai
 * created on: 2020/5/29 11:58 AM
 * description:
 */
@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {

    private Filer mFiler;

    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (CollectionUtils.isNotEmpty(annotations)) {
            Set<? extends Element> routeElements = roundEnv.getElementsAnnotatedWith(Route.class);
            try {
                this.parseRoutes(routeElements);

            } catch (Exception e) {
            }
            return true;
        }

        return false;
    }

    private void parseRoutes(Set<? extends Element> routeElements) throws IOException {
        if (CollectionUtils.isEmpty(routeElements)) {
            return;
        }
        HashMap<String, Element> nameMap = new HashMap<>();
        for (Element element : routeElements) {
            Route ann = element.getAnnotation(Route.class);
            String name = ann.path();
            nameMap.put(name, element);
        }

        ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Class.class)
        );

        ParameterSpec routeParamSpec = ParameterSpec.builder(inputMapTypeOfGroup, Consts.ROUTE_PARAM_NAME).build();

        //添加方法
        MethodSpec.Builder contractBuilder = MethodSpec.methodBuilder(Consts.ROUTE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Override.class).build())
                .addParameter(routeParamSpec);


        for (String key : nameMap.keySet()) {
            contractBuilder.addStatement(Consts.ROUTE_PARAM_NAME + ".put($S, $T.class)", key, nameMap.get(key));
        }

        MethodSpec contractName = contractBuilder.build();

        TypeName typeName = ClassName.get(elementUtils.getTypeElement(Consts.ROUTE_INTERFACE_PATH));
        //添加类
        TypeSpec typeSpec = TypeSpec.classBuilder(Consts.ROUTE_FILE_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(typeName)
                .addMethod(contractName)
                .build();

        JavaFile javaFile = JavaFile.builder(Consts.ROUTE_PACKAGE_PATH, typeSpec).build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Route.class.getCanonicalName());
    }
}
