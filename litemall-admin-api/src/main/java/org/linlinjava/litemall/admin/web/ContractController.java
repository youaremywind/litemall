package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.admin.example.PaymentHackData;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.HashMap;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.data.style.TableStyle;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import java.io.IOException;

@RestController
@RequestMapping("/admin/contracts")
@Validated
public class ContractController {
    private final Log logger = LogFactory.getLog(ContractController.class);
    @CrossOrigin
    @RequestMapping(method = {RequestMethod.POST},value = "/create",produces = "application/json;charset=UTF-8")
    public Object create(@RequestBody PaymentHackData paymentHackData) throws IOException {
//        String resource = "C:\\Users\\youar\\Desktop\\payment.docx";
        String resource = "/home/www/deploy/litemall/payment.docx";
        PaymentHackData datas =new PaymentHackData();

        Style headTextStyle = new Style();
        TableStyle headStyle = new TableStyle();
        TableStyle rowStyle = new TableStyle();
        headTextStyle.setFontFamily("Hei");
        headTextStyle.setFontSize(9);
        headTextStyle.setColor("7F7F7F");

        headStyle.setBackgroundColor("F2F2F2");
        headStyle.setAlign(STJc.CENTER);

        rowStyle = new TableStyle();
        rowStyle.setAlign(STJc.CENTER);
        if (null!=paymentHackData.getName()){
            datas.setName(paymentHackData.getName());
        }
        if (null!=paymentHackData.getTelephone()){
            datas.setTelephone(paymentHackData.getTelephone());
        }
        if (null!=paymentHackData.getName()){
            datas.setAddAmounts(paymentHackData.getName());
        }
        if (null!=paymentHackData.getName()){
            datas.setAllAmounts(paymentHackData.getName());
        }
        if (null!=paymentHackData.getName()){
            datas.setAveragePrice(paymentHackData.getName());
        }
        if (null!=paymentHackData.getTotal()){
            datas.setTotal(paymentHackData.getTotal());
        }
        if (paymentHackData.getCourses().size()>0){
            datas.setCourses(paymentHackData.getCourses());
        }
        //        输出文档
        HackLoopTableRenderPolicy hackLoopTableRenderPolicy = new HackLoopTableRenderPolicy();
        Configure config = Configure.newBuilder().bind("courses", hackLoopTableRenderPolicy).build();
        XWPFTemplate template = XWPFTemplate.compile(resource, config).render(new HashMap<String, Object>() {
            {
                put("datas", Arrays.asList(datas));
            }
        });
        template.writeToFile("out_example_payment_hack.docx");
        return ResponseUtil.ok("out_example_payment_hack.docx");
    }


}
