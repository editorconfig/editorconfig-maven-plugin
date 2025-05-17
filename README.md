# TL;DR
Official maven plugin for enforcement of rules defined in the `.editorconfig` files.

Example of plugin usage can be seen below. The configuration settings and plugin goal will be explained in the sections below:

```(xml)
<plugin>
    <groupId>org.editorconfig</groupId>
    <artifactId>editorconfig-maven-plugin</artifactId>
    <version>0.1.0.alpha1</version>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
            <phase>compile</phase>
        </execution>
        <configuration>
            <rootEditorConfigFileLocation>.editorconfig</rootEditorConfigFileLocation>
        </configuration>
    </executions>
</plugin>
```

The latest version of the plugin can be looked up in [Maven Central](https://mvnrepository.com/search?q=org.editorconfig)

## Available Goals
Currently, the plugin has the following Maven goals available:

| Goal  | Default Phase | Description                                                                                                                      |
|-------|---------------|----------------------------------------------------------------------------------------------------------------------------------|
| check | validate      | This goal checks (by default) all the files in the project for compliance with rules, defined in the appropriate `.editorconfig` |

## Configuration Parameters
The plugin has several configuration parameters

| Parameter                    | Description                                                                                                                                                                                                                                                                                                          | Default Value  |
|------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------|
| rootEditorConfigFileLocation | The location of the **root** `.editorconfig` from the **your projects Maven basedir**                                                                                                                                                                                                                                | No default     |
| strictMode                   | Whether or not to run the plugin execution in the _strict mode_. The "strict mode" means that error in the `.editorconfig` file (invalid options or their values, inlaid glob expressions etc) **will NOT** be ignored and the plugin execution **will fail** in case errors in `.editorconfig` file parsing are met | true           | 

## Supported pairs
This plugin supports **all** the key value pairs defined in the official [EditorConfig specification](https://spec.editorconfig.org/), except for the `spelling_language`. 
The reason for that is that this setting is mostly intended for the use of the code editors and IDEs, rather than the build plugins. 

