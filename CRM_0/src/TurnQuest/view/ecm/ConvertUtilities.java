package LMS.view.ecm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Properties;

import javax.faces.context.FacesContext;

import javax.servlet.ServletContext;


/*
 * Author James M Mungai
 */
public class ConvertUtilities {

    public ConvertUtilities() {
        super();
    }

    public void pdf2swf(File input, File output,
                        File finalOutput) throws IOException {
        Properties prop = new Properties();

        BufferedReader stdout = null;

        try {
            FacesContext context = FacesContext.getCurrentInstance();
            ServletContext sc =
                (ServletContext)context.getExternalContext().getContext();
            prop.load(new FileInputStream(sc.getRealPath("/config/config.properties")));
            String cmd[] =
            { prop.getProperty("swf.path"), input.getPath(), "-o",
              output.getPath() };

            String line;
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process process = pb.start();
            stdout =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
            process.destroy();
            String rfx = sc.getRealPath("/swf/rfxview.swf");
            stdout = null;
            String cmds[] =
            { prop.getProperty("swf.combine.path"), rfx, "viewport=" + output,
              "-o", finalOutput.getPath() };

            String lines;
            ProcessBuilder pbs = new ProcessBuilder(cmds);
            Process processes = pbs.start();
            stdout =
                    new BufferedReader(new InputStreamReader(processes.getInputStream()));

            while ((lines = stdout.readLine()) != null) {
                System.out.println(lines);
            }

            processes.waitFor();
            processes.destroy();

        } catch (Exception e) {
            //System.out.println(Arrays.toString(cmd));
            System.out.println(e.getMessage());
            output.delete();
            throw new IOException("Error in PDF to SWF conversion", e);
        } finally {
            //  IOUtils.closeQuietly(stdout);
        }
    }

}
