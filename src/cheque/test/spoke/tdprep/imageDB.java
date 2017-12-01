package cheque.test.spoke.tdprep;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Font;


import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.w3c.dom.Element;

import javax.imageio.IIOImage;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import com.multiconn.fop.codec.TIFFEncodeParam;
import com.multiconn.fop.codec.TIFFField;
import com.multiconn.fop.codec.TIFFImageDecoder;
import com.multiconn.fop.codec.TIFFImageEncoder;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.Base64;




/**
 *
 * @author krissada.r
 */
public class imageDB {
    

    
    private static final byte[] map = new byte[]{(byte) (255), (byte) (0)};
    private static final BufferedImage GFBufferedImage = new BufferedImage(700, 350, BufferedImage.TYPE_BYTE_GRAY);
    private static final BufferedImage BWFBufferedImage = new BufferedImage(1400,700, BufferedImage.TYPE_BYTE_BINARY,new IndexColorModel (1, 2, map, map, map));
    private static final BufferedImage BWBBufferedImage = new BufferedImage(1400,700, BufferedImage.TYPE_BYTE_BINARY,new IndexColorModel (1, 2, map, map, map));
    
    private static final BufferedImage PGFBufferedImage = new BufferedImage(810, 425, BufferedImage.TYPE_BYTE_GRAY);
    private static final BufferedImage PBWFBufferedImage = new BufferedImage(1620,850, BufferedImage.TYPE_BYTE_BINARY,new IndexColorModel (1, 2, map, map, map));
    private static final BufferedImage PBWBBufferedImage = new BufferedImage(1620,850, BufferedImage.TYPE_BYTE_BINARY,new IndexColorModel (1, 2, map, map, map));
        
    
    public static ICAS_TBL_TXN_IMAGE genGFImage(BufferedImage GFBGImage,BufferedImage BRBGImage,chequeInfo cheque,String UIC,String Proj){
        ICAS_TBL_TXN_IMAGE img = new ICAS_TBL_TXN_IMAGE();
        img.UIC = UIC;
        //Color MICRband = new Color(230, 230, 230);
        Graphics2D GFGraphics = GFBufferedImage.createGraphics();
        GFGraphics.drawImage(GFBGImage, 0 , 0 , 700, 350,null);

        GFGraphics.setColor(Color.BLACK);
        GFGraphics.setFont(new Font("Cordia New", Font.BOLD, 40));
        GFGraphics.drawString(Proj, 300, 50);
        
        //GFGraphics.setBackground(MICRband);
        //GFGraphics.clearRect(0, 290, 700, 150);

        setChequeInfoGF(GFGraphics,cheque);
        setMICR(GFGraphics,cheque);
        
        
        try {
            img.IMAGE1=writeJpegCompressedImage(GFBufferedImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
        Graphics2D BWFGraphics = BWFBufferedImage.createGraphics();
        BWFGraphics.drawImage(GFBufferedImage, 0, 0, 1400, 700, null);

        BWFGraphics.dispose();
        BWFGraphics.setComposite(AlphaComposite.Src);
        BWFGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        BWFGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        BWFGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        
        img.IMAGE2=saveTiff(BWFBufferedImage, TIFFEncodeParam.COMPRESSION_GROUP4);
        
        
        Graphics2D BWBGraphics = BWBBufferedImage.createGraphics();
        //BWBGraphics.drawImage(BRBGImage, 0, 0, 1400, 700, null);
        BWBGraphics.setBackground(Color.WHITE);
        BWBGraphics.clearRect(0, 0, 1400, 700);
        
        
        BWBGraphics.setColor(Color.BLACK);
        BWBGraphics.setFont(new Font("Times New Roman", Font.BOLD, 40));
        BWBGraphics.drawString(UIC, 500, 350);
        
        img.IMAGE3=saveTiff(BWBBufferedImage, TIFFEncodeParam.COMPRESSION_GROUP4);;
        
        return img;
    }
    
    
    public static ICAS_TBL_TXN_IMAGE genPayinImage(BufferedImage GFBGImage,String UIC,String DepAccount,Double gAmount){
        ICAS_TBL_TXN_IMAGE img = new ICAS_TBL_TXN_IMAGE();
        img.UIC = UIC;
        //BigDecimal b = new BigDecimal(gAmount);
        NumberFormat formatter=NumberFormat.getCurrencyInstance();
        
        
        Graphics2D GFGraphics = PGFBufferedImage.createGraphics();
        GFGraphics.drawImage(GFBGImage, 0 , 0 , 810, 425,null);

        
        GFGraphics.setColor(Color.BLACK);
        GFGraphics.setFont(new Font("Cordia New", Font.BOLD, 30));
        GFGraphics.drawString(DepAccount, 550, 208);
        GFGraphics.setFont(new Font("Cordia New", Font.BOLD, 30));
        GFGraphics.drawString(formatter.format(gAmount), 550, 330);
        //setChequeInfoGF(GFGraphics,cheque);
        
        
        try {
            img.IMAGE1=writeJpegCompressedImage(PGFBufferedImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
        Graphics2D BWFGraphics = PBWFBufferedImage.createGraphics();
        BWFGraphics.drawImage(PGFBufferedImage, 0, 0, 1620, 850, null);

        BWFGraphics.dispose();
        BWFGraphics.setComposite(AlphaComposite.Src);
        BWFGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        BWFGraphics.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        BWFGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        
        img.IMAGE2=saveTiff(PBWFBufferedImage, TIFFEncodeParam.COMPRESSION_GROUP4);
        
        
        Graphics2D BWBGraphics = PBWBBufferedImage.createGraphics();
        BWBGraphics.setBackground(Color.WHITE);
        BWBGraphics.clearRect(0, 0, 1620, 850);
        
        BWBGraphics.setColor(Color.BLACK);
        BWBGraphics.setFont(new Font("Times New Roman", Font.BOLD, 40));
        BWBGraphics.drawString(UIC, 500, 350);
        
        img.IMAGE3=saveTiff(PBWBBufferedImage, TIFFEncodeParam.COMPRESSION_GROUP4);;
        
        return img;
    }
    
    
    public static String writeJpegCompressedImage(BufferedImage image) throws IOException {
        
        String encoded="";
        
        try {
            
            final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
            
            writer.setOutput(ios);

            IIOMetadata imageMetaData = writer.getDefaultImageMetadata(new ImageTypeSpecifier(image), null);
            Element tree = (Element) imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
            Element jfif = (Element)tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(100));
            jfif.setAttribute("Ydensity", Integer.toString(100));
            jfif.setAttribute("resUnits", "1");
            imageMetaData.setFromTree("javax_imageio_jpeg_image_1.0", tree);
        
            JPEGImageWriteParam params = new JPEGImageWriteParam(null);
            params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            params.setCompressionQuality(0.9f);

            writer.write(imageMetaData, new IIOImage(image, null, imageMetaData), params);
            
            return Base64.getEncoder().encodeToString(baos.toByteArray());
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return encoded;

  }
    
    public static void setChequeInfoGF (Graphics2D graphic,chequeInfo cheque){
        
        NumberFormat formatter=NumberFormat.getCurrencyInstance();
        
        
        
        graphic.setColor(Color.BLACK);
        graphic.setFont(new Font("Cordia New", Font.BOLD, 30));
        graphic.drawString(formatter.format(Double.valueOf(cheque.sAmount)), 450, 165);
        
        graphic.setFont(new Font("Cordia New", Font.BOLD, 30));
        graphic.drawString(cheque.sChequeDate, 480, 33);
        
        graphic.setFont(new Font("Cordia New", Font.PLAIN, 24));
        String ThaiBaht = new getThaiBaht().getText(cheque.sAmount);
        graphic.drawString("-"+ThaiBaht+"-", 128, 130);
       
        

    }
    
    public static void setMICR (Graphics2D graphic,chequeInfo cheque){
        String MICR;
        MICR = "A"+cheque.sCRC+" C"+cheque.sChequeNo+"C"+cheque.sBankNo+"D"+cheque.sBranchNo+"A "+cheque.sAccountNo+"C"+cheque.sDocType;//+"B"+"cheque.sAmountB";
        graphic.setColor(Color.BLACK);
        graphic.setFont(new Font("MICR Encoding", Font.BOLD, 24));
        graphic.drawString(MICR, 30, 325);
    }
    
    
    
    private static String saveTiff (BufferedImage bufferedImage, int compression)   {
            
        TIFFEncodeParam tiffEncodeParam = new TIFFEncodeParam ();
        tiffEncodeParam.setCompression (compression);
        tiffEncodeParam.setWriteTiled (false);
        
        FileOutputStream file=null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        long[][] xResolution = {{200, 1}};
        long[][] yResolution = {{200, 1}};
        char[] resolutionUnit = {2};

        tiffEncodeParam.setTileSize (0, 700);

        TIFFField[] extraFields = new TIFFField[3];
        extraFields[0] = new TIFFField (TIFFImageDecoder.TIFF_X_RESOLUTION, TIFFField.TIFF_RATIONAL, 1, xResolution);
        extraFields[1] = new TIFFField (TIFFImageDecoder.TIFF_Y_RESOLUTION, TIFFField.TIFF_RATIONAL, 1, yResolution);
        extraFields[2] = new TIFFField (TIFFImageDecoder.TIFF_RESOLUTION_UNIT, TIFFField.TIFF_SHORT, 1, resolutionUnit);
        tiffEncodeParam.setExtraFields (extraFields);

        TIFFImageEncoder tiffImageEncoder = null;
        //tiffImageEncoder = new TIFFImageEncoder (baos, tiffEncodeParam);
        
        tiffImageEncoder = new TIFFImageEncoder (baos, tiffEncodeParam);
		try {
			tiffImageEncoder.encode (bufferedImage);
                        
                        byte[] bytearray = baos.toByteArray();
                        
                        baos.close();
                        
                        return Base64.getEncoder().encodeToString(bytearray);
                        
		} catch (IOException ex) {
			System.out.println(ex);
		}
                
                
	return "";	
	
    }

    
        public static int[] getRGB (int x, int y) {
                int[] pixels;
                long pixelsSize = 1400 * 700 * 3;
                pixels = new int[(int) pixelsSize];
		int[] rgb = new int[3];
		System.arraycopy (pixels, (x * 700 + y) * 3, rgb, 0, 3);
		return rgb;
	}

    
}
