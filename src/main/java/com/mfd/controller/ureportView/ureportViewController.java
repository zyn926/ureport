package com.mfd.controller.ureportView;

import com.bstek.ureport.export.ExportManager;
import com.bstek.ureport.export.html.HtmlReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "ureportView")
public class ureportViewController {

    @Autowired
    private ExportManager exportManager;

    @RequestMapping(value = "/page")
    public ModelAndView page(HttpServletRequest request,@RequestParam("reportName") String reportName){
        ModelAndView view = new ModelAndView("ureportView/ureportPage");
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("qtr",3);
        HtmlReport htmlReport = exportManager.exportHtml("database:"+reportName+".ureport.xml",request.getContextPath(),parameters);
        StringBuffer content = new StringBuffer();
        content.append("<style type='text/css'>");
        content.append(htmlReport.getStyle());
        content.append("</style>");
        content.append(htmlReport.getContent());
        view.addObject("report",content.toString());
        return view;
    }

    /*@RequestMapping(value = "/getContent")
    @ResponseBody
    public String getReport(HttpServletRequest request){

        return content.toString();
    }*/


}
