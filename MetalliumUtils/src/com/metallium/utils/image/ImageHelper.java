/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.metallium.utils.image;

import com.metallium.utils.image.encoders.JpegEncoder;
import com.metallium.utils.framework.utilities.LogHelper;
import com.metallium.utils.utils.FileUtil;
import com.jhlabs.image.CropFilter;
import com.jhlabs.image.SharpenFilter;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

/**
 * 20110128
 * @author Ruben
 */
public class ImageHelper {

//    private static float imageQuality = 0.94F;
    private static float imageQuality = 0.94F;

    public static void main(String[] args) {
        test4ScaleImageCrop();
        //   test2();
        //   test3ReadImage();
    }

    public static void test3ReadImage() {

        long startTime = System.currentTimeMillis();
        long endTime;

        String source = "c://DSC_01.jpg";
        File sourceFile = new File(source);
        try {
            BufferedImage bufferedImage = ImageIO.read(sourceFile);
        } catch (IOException ex) {
            LogHelper.makeLog(ex);
        }

        endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LogHelper.makeLog("TotalRead: " + duration);

    }

    public static void test1ScaleImage() {
        String source0 = "c://img01.jpg";
        String source = "c://img01.jpg";
        String target = "c://img02.jpg";
        int width = 700;

        try {

//            source = "c://DSCF0081.jpg";

            long startTime = System.currentTimeMillis();
            long endTime;

            File sourceFile = new File(source);
            File targetFile = new File(target);

            processImageDimensions(new File(source0), sourceFile, 720, false);
            processImageDimensions(sourceFile, targetFile, 150, true);

            endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            LogHelper.makeLog("Total: " + duration);

        } catch (Exception e) {
            LogHelper.makeLog(e);
        }
    }

    public static void test2() {
        String source = "c://source.jpg"; //I get a normal image and I compress it.
        String dest = "c://source2.jpg";
        int width = 200;

        try {

            File sourceFile = new File(source);
            writeImage(sourceFile, dest);

        } catch (Exception e) {
            LogHelper.makeLog(e);
        }

    }

    public static void test4ScaleImageCrop() {
        String source = "c://img01.jpg";
        String target = "c://img02.jpg";

        source = "c://01.jpg";
        target = "c://02.jpg";

        source = "c://t_slash.jpg";
        target = "c://t_slash_.jpg";

        int width = 150;

        try {
            long startTime = System.currentTimeMillis();
            long endTime;

            File sourceFile = new File(source);
            File targetFile = new File(target);

         //   processImageDimensions_(sourceFile, targetFile, width, 80, true);

            endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            LogHelper.makeLog("Total: " + duration);

        } catch (Exception e) {
            LogHelper.makeLog(e);
        }
    }

    /**
     *
     * Process Image means that Im going to get an Image File, and Im going to see if that image fits
     * in the desired width. If the image doesn't fit in the desired with then I scale the image.
     * All modifications are done to in the files location.
     *
     * This means, that for reading and writing purposes the Source and the Target are going to be the same.
     * I Modify the same file.
     *
     * @param image
     * @param maxWidth
     * @throws Exception if something bad happened.
     */
    public static void processImageDimensions(File image, int maxWidth, boolean squareImage) throws Exception {
        processImageDimensions(image, image, maxWidth, squareImage);
    }


    /**
     * This transforms the image to a new size dimension
     *
     * @param source
     * @param target
     * @param maxWidth
     * @param squareImage
     * @throws Exception
     */
    public static void processImageDimensions(File source, File target, int maxWidth, boolean squareImage) throws Exception {

        BufferedImage bufferedImage = ImageIO.read(source);

        if (shouldIscale(bufferedImage, maxWidth)) {
            bufferedImage = scaleImage(bufferedImage, maxWidth, squareImage); //Ok now I have the image scaled.
        }

        //Even if the file doesn't need to be scaled I write it with the encoder so it can be compressed.

        //write(bufferedImage, 0.94F, file); //Doesnt compress the JPG well
        writeWithJpegEncoder(bufferedImage, target); //Very good compression quality

    }

    /**
     * This transforms the image to a new size dimension
     *
     * @param source
     * @param target
     * @param maxWidth
     * @param cropHeight
     * @throws Exception
     */
    public static void processImageDimensions(File source, File target, int maxWidth, int cropHeight) throws Exception {

        BufferedImage bufferedImage = ImageIO.read(source);

        if (shouldIscale(bufferedImage, maxWidth)) {
            bufferedImage = scaleImage(bufferedImage, maxWidth, cropHeight, true); //Ok now I have the image scaled.
        }

        //Even if the file doesn't need to be scaled I write it with the encoder so it can be compressed.

        //write(bufferedImage, 0.94F, file); //Doesnt compress the JPG well
        writeWithJpegEncoder(bufferedImage, target); //Very good compression quality

    }





    /**
     * Verifies if an image is wider than a certain size if it is, then it scales the image.
     *
     * @param image =
     * @param scaleSize
     * @return
     * @throws IOException
     */
    private static boolean shouldIscale(BufferedImage bsrc, int scaleSize) throws IOException {
        boolean answer = false;


        int imageWidth = bsrc.getWidth();
        if (scaleSize < imageWidth) {
            answer = true;
        }


        return answer;
    }

    /**
     * Scales an Image to a desired size and applies other filters to make an image look
     * better like for example a sharpen filter.
     *
     * @param bufferedImage to scale
     * @param width to scale image to
     * @param squareImage indicates if the image should be scaled to have the same height and width
     * @return BufferedImage with the desired size
     * @throws Exception
     */

    private static BufferedImage scaleImage(BufferedImage bufferedImage, int width) throws Exception {

        int thumbWidth = width;

        //I calculate the height of the image based on the desired width.
        int thumbHeight = calculateNewHeight(bufferedImage.getWidth(), bufferedImage.getHeight(), width);

        bufferedImage = scaleImageAlgorithm(bufferedImage, thumbWidth, thumbHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

        //Once I get the image I apply a Sharpen Filter to make it look better.
        SharpenFilter sharpF = new SharpenFilter();
        bufferedImage = sharpF.filter(bufferedImage, null);


        return bufferedImage;

    }


    private static BufferedImage scaleImage(BufferedImage bufferedImage, int width, boolean squareImage) throws Exception {

        int thumbWidth = width;

        //I calculate the height of the image based on the desired width.
        int thumbHeight = calculateNewHeight(bufferedImage.getWidth(), bufferedImage.getHeight(), width);

        if (squareImage) {
            //If the image is going to have the same width and height like a square, then, I have to verify which side is longer,
            //if the width or the hieght. In order to give the smaller one of these two the value in the 'width' attribute
            //and recalcute the other (height or width, which ever is longer)
            if (thumbHeight < width) {
                thumbHeight = width;
                thumbWidth = calculateNewWidth(bufferedImage.getWidth(), bufferedImage.getHeight(), width);
            }
        }

        bufferedImage = scaleImageAlgorithm(bufferedImage, thumbWidth, thumbHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

        if (squareImage) {
            CropFilter cropF = new CropFilter(0, 0, width, width);
            bufferedImage = cropF.filter(bufferedImage, null);
        }

        //Once I get the image I apply a Sharpen Filter to make it look better.
        SharpenFilter sharpF = new SharpenFilter();
        bufferedImage = sharpF.filter(bufferedImage, null);


        return bufferedImage;

    }


    private static BufferedImage scaleImage(BufferedImage bufferedImage, int width, int cropHeight,  boolean squareImage) throws Exception {

        int thumbWidth = width;

        //I calculate the height of the image based on the desired width.
        int thumbHeight = calculateNewHeight(bufferedImage.getWidth(), bufferedImage.getHeight(), width);

        if (squareImage) {
            //If the image is going to have the same width and height like a square, then, I have to verify which side is longer,
            //if the width or the hieght. In order to give the smaller one of these two the value in the 'width' attribute
            //and recalcute the other (height or width, which ever is longer)
            if (thumbHeight < width) {
                thumbHeight = width;
                thumbWidth = calculateNewWidth(bufferedImage.getWidth(), bufferedImage.getHeight(), width);
            }
        }

        bufferedImage = scaleImageAlgorithm(bufferedImage, thumbWidth, thumbHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);

        if (squareImage) {
            CropFilter cropF = new CropFilter(0, 0, width, cropHeight);
            bufferedImage = cropF.filter(bufferedImage, null);
        }

        //Once I get the image I apply a Sharpen Filter to make it look better.
        SharpenFilter sharpF = new SharpenFilter();
        bufferedImage = sharpF.filter(bufferedImage, null);


        return bufferedImage;

    }


    /**
     *
     * @param image
     * @param dest
     * @return
     * @throws Exception
     */
    public static void writeImage(File image, String dest) throws Exception {

        BufferedImage bufferedImage = ImageIO.read(image);
        File destFile = new File(dest);
        //    write(bufferedImage, imageQuality, file);
        writeWithJpegEncoder(bufferedImage, destFile);
    }

    /**
     * Writes an Image to Disk using an Encoder so that the image can be compressed
     * It uses the Default quality to compress, which is very good.
     *
     * @param image
     * @param fileDestination
     * @return
     * @throws Exception
     */
    private static boolean writeWithJpegEncoder(Image image, File fileDestination) throws Exception {

        FileUtil.verifyFileDestination(fileDestination);    //I verify the destination location is ready.

        OutputStream output = null;
        try {
            output = new BufferedOutputStream(new FileOutputStream(fileDestination));
            JpegEncoder encoder = new JpegEncoder(image, 82, output);
            encoder.Compress();
        } finally {
            //Close resources

            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception ex) {
            }
        }

        return true;
    }

    /**
     *  Writes an image to DIsk using a standard compression algorithm.
     *  The compression is not good at all. Please use "writeWithJpegEncoder" instead
     *
     * @param image im going to save.
     * @param quality of the image Im going to save.
     * @param fileDestination where Im going to save the image.
     * @throws IOException if something fucked up happened
     */
    private static void write(RenderedImage image, float quality, File fileDestination) throws IOException {


        ImageWriter writer = null;
        Iterator iter = ImageIO.getImageWritersByFormatName("JPEG");
        if (!iter.hasNext()) {
            throw new IOException("No Writers Available");
        }

        writer = (ImageWriter) iter.next();

        FileUtil.verifyFileDestination(fileDestination);    //I verify the destination location is ready.

        ImageOutputStream ios = null;
        try {
            ios = ImageIO.createImageOutputStream(fileDestination);
            writer.setOutput(ios);

            JPEGImageWriteParam iwp = new JPEGImageWriteParam(null);
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(quality);
            writer.write(null, new IIOImage(image, null, null), iwp);
            ios.flush();

        } finally {
            if (writer != null) {
                writer.dispose();
            }

            if (ios != null) {
                ios.close();
            }
        }

    }

    /**
     * This function calculates the Height of a new image, based on the actual size
     * of the image and the new desired WIDTH. This new Height we are going to
     * calculate keeps the proportions of the image (keeps the same size ratio).
     *
     *
     * @param imageWidth = actual image width
     * @param imageHeight = actual image height
     * @param newWidth = new desired width
     * @return new height.
     */
    private static int calculateNewHeight(int imageWidth, int imageHeight, int newWidth) {
        double aspectRatio = (double) imageHeight / (double) imageWidth;
        int newHeight = (int) (aspectRatio * (double) newWidth);
        return newHeight;
    }

    private static int calculateNewWidth(int imageWidth, int imageHeight, int newHeight) {
        double aspectRatio = (double) imageWidth / (double) imageHeight;
        int newWidth = (int) (aspectRatio * (double) newHeight);
        return newWidth;
    }

    /**
     * Algorithm to scale an image
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in down-scaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@codey BufferedImage}
     */
    private static BufferedImage scaleImageAlgorithm(BufferedImage img,
            int targetWidth,
            int targetHeight,
            Object hint,
            boolean higherQuality) {
        int type = (img.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        int w, h;
        if (higherQuality && (img.getWidth() > targetWidth && img.getHeight() > targetHeight)) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }

        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, null);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
