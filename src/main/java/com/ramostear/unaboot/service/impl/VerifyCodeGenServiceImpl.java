package com.ramostear.unaboot.service.impl;

import com.ramostear.unaboot.common.util.RandomUtils;
import com.ramostear.unaboot.domain.valueobject.VerifyCodeVo;
import com.ramostear.unaboot.service.VerifyCodeGenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @ClassName VerifyCodeGenServiceImpl
 * @Description 验证码服务类
 * @Author 树下魅狐
 * @Date 2020/2/28 0028 7:23
 * @Version since UnaBoot-1.0
 **/
@Slf4j
@Service("verifyCodeGenService")
public class VerifyCodeGenServiceImpl implements VerifyCodeGenService {

    private static final String[] FONT_TYPES = {"\u5b8b\u4f53", "\u65b0\u5b8b\u4f53", "\u9ed1\u4f53", "\u6977\u4f53", "\u96b6\u4e66"};
    private static final int DEFAULT_CODE_LENGTH = 4;

    @Override
    public String generate(int width, int height, OutputStream outputStream) throws IOException {
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        fillBackground(graphics,width,height);
        String code = RandomUtils.randomStr(DEFAULT_CODE_LENGTH);
        createCharacter(graphics,code);
        graphics.dispose();
        ImageIO.write(image,"JPEG",outputStream);
        return code;
    }

    @Override
    public VerifyCodeVo generate(int width, int height){
        VerifyCodeVo codeVo = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String code = generate(width,height,byteArrayOutputStream);
            codeVo = new VerifyCodeVo();
            codeVo.setCode(code);
            codeVo.setImgBytes(byteArrayOutputStream.toByteArray());
        }catch (IOException e){
            log.error("generate verify code error : {}",e.getMessage());
        }
        return codeVo;
    }


    private static void fillBackground(Graphics graphics,int width,int height){
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0,width,height);
        for(int i=0;i<8;i++){
            graphics.setColor(RandomUtils.randomColor(40,150));
            Random random = new Random();
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int w = random.nextInt(width);
            int h = random.nextInt(height);
            graphics.drawLine(x,y,w,h);
        }
    }

    private void createCharacter(Graphics g,String code){
        char[] chars = code.toCharArray();
        for(int i = 0; i<chars.length;i++){
            g.setColor(RandomUtils.randomColor(50,100));
            g.setFont(new Font(FONT_TYPES[RandomUtils.nextInt(FONT_TYPES.length)],Font.BOLD,26));
            g.drawString(String.valueOf(chars[i]),15*i+5,19+RandomUtils.nextInt(8));
        }
    }
}
