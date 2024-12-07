package cn.winfxk.lexy.amp.tool.view.image;

import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.StartView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageView extends MyJPanel implements MouseListener, StartView {
    public static final File CacheDir = new File(new File(System.getProperty("user.home")), "Winfxk/Cache");
    private volatile boolean download = false;
    private final JLabel imageView, textView;
    private OnClickListener onClick;
    private ImageIcon image;
    private Stretch stretch;
    private int padding = 5;

    static {
        if (!CacheDir.exists() || !CacheDir.isDirectory()) CacheDir.mkdirs();
    }

    public OnClickListener getOnClick() {
        return onClick;
    }

    public void setOnClick(OnClickListener onClick) {
        this.onClick = onClick;
    }

    public ImageView() {
        this((ImageIcon) null, Stretch.Fit);
    }

    public ImageView(BufferedImage image) {
        this(image, Stretch.Fit);
    }

    public ImageView(File file) throws IOException {
        this(ImageIO.read(file));
    }

    public ImageView(BufferedImage image, Stretch stretch) {
        this(image == null ? null : getIcon(image), stretch);
    }

    public ImageView(ImageIcon image, Stretch stretch) {
        this.image = image;
        this.stretch = stretch == null ? Stretch.Fit : stretch;
        imageView = new JLabel("", SwingConstants.CENTER);
        imageView.setOpaque(false);
        imageView.setLocation(0, 0);
        textView = new JLabel("", SwingConstants.CENTER);
        textView.setOpaque(false);
        textView.setLocation(0, 0);
        setOpaque(false);
        add(textView, CENTER_ALIGNMENT);
        add(imageView, CENTER_ALIGNMENT);
        onClick = null;
        addMouseListener(this);
    }

    public void setText(String text) {
        if (textView != null) textView.setText(text);
    }

    public void setFont(Font font) {
        if (textView != null)
            textView.setFont(font);
    }

    public void setTextColor(Color color) {
        if (textView != null)
            textView.setForeground(color);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (onClick != null && isEnabled()) onClick.onClick(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * @return 内边距
     */
    public int getPadding() {
        return padding;
    }

    /**
     * 设置内边距
     *
     * @param padding
     */
    public void setPadding(int padding) {
        this.padding = padding;
    }

    @Override
    public void start() {
        imageView.setSize(width = getWidth(), height = getHeight());
        textView.setSize(imageView.getSize());
        if (image != null) {
            int newWidth, newHeight;
            int imageWidth = Math.max(width - padding, 1);
            int imageHeight = Math.max(height - padding, 1);
            switch (stretch) {
                case FullScreen:
                    newWidth = imageWidth;
                    newHeight = imageHeight;
                    break;
                case Tiled:
                    if (image.getIconWidth() < image.getIconHeight()) {
                        newWidth = imageWidth;
                        newHeight = (int) (image.getIconHeight() * ((double) imageWidth / image.getIconWidth()));
                    } else {
                        newHeight = imageHeight;
                        newWidth = (int) (image.getIconWidth() * ((double) imageHeight / image.getIconHeight()));
                    }
                    break;
                case Original:
                    newWidth = image.getIconWidth();
                    newHeight = image.getIconHeight();
                    break;
                case Fit:
                default:
                    double ratio = (double) image.getIconWidth() / imageWidth;
                    if (image.getIconHeight() / ratio > imageHeight) {
                        newHeight = imageHeight;
                        ratio = (double) image.getIconHeight() / imageHeight;
                        newWidth = (int) (image.getIconWidth() / ratio);
                    } else {
                        newWidth = imageWidth;
                        ratio = (double) image.getIconWidth() / imageWidth;
                        newHeight = (int) (image.getIconHeight() / ratio);
                    }
            }
            imageView.setIcon(getIcon(image.getImage(), Math.max(newWidth, 1), Math.max(newHeight, 1)));
        } else imageView.setIcon(null);
    }

    /**
     * 设置图片
     *
     * @param image 图片Icon对象
     */
    public void setImage(ImageIcon image) {
        this.image = image;
    }

    /**
     * 设置图片
     *
     * @param Url 图片的HTTP地址
     */
    public void setImage(String Url) {
        if (download || Url == null || Url.isEmpty()) return;
        download = true;
        new Thread(() -> {
            try {
                File cache = getCacheFile();
                if (Url.toLowerCase().startsWith("https"))
                    Tool.downLoadFromUrlHttps(Url, cache.getName(), cache.getParent());
                else Tool.DownFile(Url, cache.getName(), cache.getParent());
                setImage(cache);
                cache.delete();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                download = false;
            }
        }).start();
    }

    /**
     * 设置图片
     *
     * @param image
     */
    public void setImage(InputStream image) throws IOException {
        setImage(ImageIO.read(image));
    }

    /**
     * 设置图片
     *
     * @param image 图片File对象
     */
    public void setImage(File image) throws IOException {
        setImage(ImageIO.read(image));
    }

    /**
     * 设置图片
     *
     * @param image 图片
     */
    public void setImage(BufferedImage image) {
        this.image = getIcon(image);
    }


    /**
     * @return 返回图片
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     * 设置图片拉伸方式
     *
     * @param stretch
     */
    public void setStretch(Stretch stretch) {
        this.stretch = stretch;
    }

    /**
     * @return 图片拉伸方式
     */
    public Stretch getStretch() {
        return stretch;
    }

    /**
     * @return 返回一个缓存文件
     */
    public File getCacheFile() {
        String Filename = Tool.getRandString();
        File file = new File(CacheDir, Filename);
        while (file.exists() && file.isFile()) {
            Filename += Tool.getRandString();
            file = new File(CacheDir, Filename);
        }
        return file;
    }
}
