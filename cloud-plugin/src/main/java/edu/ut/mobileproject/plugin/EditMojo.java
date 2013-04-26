package edu.ut.mobileproject.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * Goal that processes @Cloud annotations in android development.
 *
 * @goal edit
 * @phase process-sources
 */
public class EditMojo extends AbstractMojo {
    private static String PATTERN = ".java";
    static StringBuilder sb = new StringBuilder();


    /**
     * Location of the files processed after processing.
     *
     * @parameter expression="${edit.destination}"  default-value="${project.build.sourceDirectory}"
     */
    private File destination;


    /**
     * The Location of the file to edit.
     *
     * @parameter expression="${edit.source}"  default-value="${project.build.sourceDirectory}"
     * @required
     */
    private File source;


    /**
     * Searches for all possible @Cloud annotations
     * and process them as needed
     */
    public void execute() throws MojoExecutionException {
        Path startingDir = Paths.get(source.getPath());
        Finder finder = new Finder(PATTERN);
        try {
            Files.walkFileTree(startingDir, finder);
        } catch (IOException e) {
            getLog().error("Cannot process file", e);
        }
        finder.done();
        System.out.println(sb.toString());

    }

    /**
     * A {@code FileVisitor} that finds all files that match the specified
     * pattern.
     */
    public static class Finder extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;
        private int numMatches = 0;

        Finder(String pattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }

        // Compares the glob pattern against
        // the file or directory name.
        void find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                System.out.println(file);
            }
        }

        private static void writeToFile(Path file, byte[] data) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            try {

                FileChannel fc = (FileChannel.open(file, StandardOpenOption.READ, StandardOpenOption.WRITE));
                fc.position(0);
                while (byteBuffer.hasRemaining()) {
                    fc.write(byteBuffer);
                }

            } catch (IOException e) {

            }

        }


        private String changePrimitiveToWrapper(String str) {

            if ("long".equalsIgnoreCase(str)) {
                return "Long";
            } else if ("int".equalsIgnoreCase(str)) {
                return "Integer";
            } else if ("char".equalsIgnoreCase(str)) {
                return "Character";
            } else if ("byte".equalsIgnoreCase(str)) {
                return "Byte";
            } else if ("float".equalsIgnoreCase(str)) {
                return "Float";
            } else if ("double".equalsIgnoreCase(str)) {
                return "Double";
            } else if ("void".equalsIgnoreCase(str)) {
                return null;
            }
            return str;
        }

        // Prints the total number of
        // matches to standard out.
        void done() {
            System.out.println("Number of files Matched: " + numMatches);
        }

        // Invoke the pattern matching
        // method on each file.
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

            find(file);

            InputStream in;
            OutputStream out;
            try {
                in = Files.newInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));


                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException x) {
                System.err.println(x);
            }

            String strPlay = sb.toString();


            //clear file
            for (int i = 0; i < sb.length(); i++) {
                sb.replace(i, i + 1, " ");
            }
            writeToFile(file, sb.toString().getBytes());
            sb.delete(0, sb.length());

            sb.append(strPlay);

            //Search for the key word extends CloudService
            Pattern extendPattern = Pattern.compile("extends\\s*CloudActivity");
            Matcher extendMatcher = extendPattern.matcher(sb);

            if(extendMatcher.find()){
                //Process the necessary classes
                Pattern pattern = Pattern.compile("@Cloud\\s*((private)|(public)|(protected))+(\\s*\\w*){2}\\s*\\((\\s*\\w*\\s{1,}\\w*\\s*(,)*)*\\)(\\s*\\w*)*\\{");
                Matcher matchers = pattern.matcher(sb);

                boolean found = false;
                int count = 0;
                while (matchers.find()) {
                    System.out.format("I found the text \"%s\" starting at index %d and ending at index %d.%n", matchers.group(), matchers.start(), matchers.end());
                    //edit the class

                    Pattern params = Pattern.compile("\\((\\s*\\w*\\s{1,}\\w*\\s*(,)*)*\\)");
                    Matcher mparams = params.matcher(matchers.group());
                    mparams.find();
                    String paramTypes = "", paramValues = "";
                    String[] typesAndParams = mparams.group().replace("(", "").replace(")", "").split(",");
                    String stp = "", stpValues = "";


                    for (int i = 0; i < typesAndParams.length; i++) {
                        String[] st = typesAndParams[i].split("\\s");
                        if (st.length > 0) {
                            paramTypes += changePrimitiveToWrapper(st[0]) + ".class, ";
                            paramValues += st[1] + ",";
                        }
                    }
                    stp = paramTypes.substring(0, paramTypes.lastIndexOf(",") > 0 ? paramTypes.lastIndexOf(",") : 0);
                    stpValues = paramValues.substring(0, paramValues.lastIndexOf(",") > 0 ? paramValues.lastIndexOf(",") : 0);

                    // }


                    //get ReturnType for Method
                    Pattern retType = Pattern.compile("((private)|(public)|(protected))+(\\s*\\w*){2}");
                    Matcher mret = retType.matcher(matchers.group());
                    String retString = "";
                    if (mret.find()) {
                        String[] retS = mret.group().split("\\s");
                        if (retS.length == 3) {
                            retString = changePrimitiveToWrapper(retS[retS.length - 2]);
                        }
                    }


                    //Rename method
                    Pattern methodNamePattern = Pattern.compile("\\w*\\s*\\(");
                    Matcher m = methodNamePattern.matcher(matchers.group());
                    m.find();
                    System.out.format("I found M \"%s\" starting at index %d and ending at index %d.%n", m.group(), m.start(), m.end());
                    StringBuilder sm = new StringBuilder(m.group());
                    sm.insert(0, "local");


                    String originalName = matchers.group();

                    sb.replace(matchers.start() + matchers.group().indexOf(m.group()), matchers.start() + matchers.group().indexOf(m.group()) + m.group().length(), sm.toString());

                    String paramType, paramValue, paramV;

                    if (!stp.isEmpty()) {
                        paramType = "Class<?>[] paramTypes = {" + stp + "}; \n";
                    } else {
                        paramType = "Class<?>[] paramTypes = null; \n";
                    }

                    if (!stpValues.isEmpty()) {
                        paramV = "Object[] paramValues = {" + stpValues + "}; \n";
                    } else {
                        paramV = "Object[] paramValues = null; \n";
                    }

                    if (retString != null) {
                        paramValue = " result = (" + retString + ") getCloudController().execute(toExecute,paramValues,this);\n";
                    } else {
                        paramValue = " result = null;\n";
                    }


                    String str = "\n" + originalName.replace("@Cloud", "")
                            + "\n" +
                            "\tMethod toExecute; \n\t" +
                            paramType +
                            "\t" +
                            paramV +
                            "\t" +
                            retString + " result = null;\n" +
                            "\t" +
                            "try{\n" +
                            "\t" +
                            "toExecute = this.getClass().getDeclaredMethod(\"" + sm.toString().substring(0, sm.length() - 1) + "\", paramTypes);\n" +
                            "\t" +

                            paramValue +

                            "\t" +
                            "}  catch (SecurityException se){\n" +
                            "\t" +
                            "} catch (NoSuchMethodException ns){\n" +
                            "\t" +
                            "}catch (Throwable th){\n" +
                            "\t" +
                            "}\n" +
                            "\t" +
                            "return result;\n" +
                            "\t" +
                            "}\n" +
                            "";

                    sb.insert(sb.lastIndexOf("}") - 1, str);
                    count++;

                }

                int i = sb.lastIndexOf("import");
                sb.insert(i,"import java.lang.reflect.Method; \n");
                System.out.println("Count of Matches :" + count);
            }

            writeToFile(file, sb.toString().getBytes());
            sb.delete(0,sb.length());
            return CONTINUE;
        }

        // Invoke the pattern matching
        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.err.println(exc);
            return CONTINUE;
        }
    }

}
