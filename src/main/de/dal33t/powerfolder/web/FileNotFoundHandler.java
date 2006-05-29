package de.dal33t.powerfolder.web;

import java.io.StringWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import de.dal33t.powerfolder.Controller;

/**
 * Shows the 404 message if the requested file is not found. The template used
 * is: 404.vm
 * 
 * @author <A HREF="mailto:schaatser@powerfolder.com">Jan van Oosterom</A>
 */
public class FileNotFoundHandler implements Handler {

    public HTTPResponse getPage(HTTPRequest httpRequest) {

        /* lets make a Context and put data into it */
        VelocityContext context = new VelocityContext();
        context.put("PowerFolderVersion", Controller.PROGRAM_VERSION);

        /* lets render a template */
        StringWriter writer = new StringWriter();
        try {
            Velocity.mergeTemplate("web-resources/templates/404.vm", Velocity.ENCODING_DEFAULT,
                context, writer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        HTTPResponse response = new HTTPResponse(writer.toString());
        response.setResponseCode(HTTPConstants.HTTP_NOT_FOUND);
        return response;
    }
}
