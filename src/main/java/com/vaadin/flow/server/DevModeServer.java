package com.vaadin.flow.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DevModeServer implements Serializable {

    private static final int DEFAULT_BUFFER_SIZE = 32 * 1024;
    private static final Integer WEBPACK_PORT = 8081;

    private final int bufferSize;

    private static Process exec;

    public DevModeServer() {
        this(DEFAULT_BUFFER_SIZE);

        if (exec != null && exec.isAlive()) {
            System.out.println("Webpack is already running.");
            return;
        }

        System.out.println("Starting Webpack in dev mode ...");
        try {
            File directory = new File("src/main/webapp").getAbsoluteFile();
            if (!directory.exists()) {
                throw new RuntimeException("Cannot change to " + directory);
            }

            File webpack = new File(directory.getAbsolutePath() + "/node_modules/.bin/webpack-dev-server");
            if (!webpack.canExecute()) {
                throw new RuntimeException("Cannot execute " + webpack);
            }

            System.err.println(webpack.getAbsolutePath());
            System.err.println(System.getenv("PATH"));

            ProcessBuilder process = new ProcessBuilder();
            process.directory(directory);

            // Add /usr/local/bin to the PATH in case of unixOS like
            File shell = new File("/bin/sh");
            if (shell.canExecute()) {
                process.environment().put("PATH",
                        "node_modules/.bin:" + process.environment().get("PATH") + ":/usr/local/bin");
            }

            process.command(
                    new String[] { webpack.getAbsolutePath(), "--port", WEBPACK_PORT.toString(), "--colors", "false" });

            exec = process.start();

            Runtime.getRuntime().addShutdownHook(new Thread(exec::destroy));

            new Thread(() -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                try {
                    for (String line; ((line = reader.readLine()) != null);) {
                        System.err.write(line.getBytes());
                        System.err.write('\n');
                    }
                } catch (IOException e) {
                }
            }).start();

            // Webpack takes a couple of seconds to be ready
            Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DevModeServer(int bufferSize) {
        this.bufferSize = DEFAULT_BUFFER_SIZE;
    }

    public boolean isDevModeRequest(HttpServletRequest request) {
        String requestFilename = getRequestFilename(request);
        return requestFilename.equals("/index.js");
    }

    public void serveFrontendFile(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String requestFilename = getRequestFilename(request);

        URL uri = new URL("http://localhost:" + WEBPACK_PORT + requestFilename);
        // Debug
        System.err.println("Requested: " + uri);

        HttpURLConnection connection = (HttpURLConnection)uri.openConnection();
        connection.setRequestMethod(request.getMethod());
        connection.setReadTimeout(60 * 1000);
        connection.setConnectTimeout(60 * 1000);
        // Copies all the headers from the original request
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            connection.setRequestProperty(header, request.getHeader(header));
        }
        // Copies response headers
        connection.getHeaderFields().forEach((header, values) -> {
            if (header != null) {
                response.addHeader(header, values.get(0));
            }
        });
        // Copies response payload
        writeStream(response.getOutputStream(), connection.getInputStream());
        // Copies response code
        response.sendError(connection.getResponseCode());
    }

    private void writeStream(ServletOutputStream outputStream, InputStream inputStream) throws IOException {
        final byte[] buffer = new byte[bufferSize];
        int bytes;
        while ((bytes = inputStream.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, bytes);
        }
    }

    /**
     * Returns the (decoded) requested file name, relative to the context path.
     * <p>
     * Package private for testing purposes.
     *
     * @param request
     *            the request object
     * @return the requested file name, starting with a {@literal /}
     */
    String getRequestFilename(HttpServletRequest request) {
        // http://localhost:8888/context/servlet/folder/file.js
        // ->
        // /servlet/folder/file.js

        return request.getPathInfo() == null ? request.getServletPath()
                : request.getServletPath() + request.getPathInfo();
    }
}
