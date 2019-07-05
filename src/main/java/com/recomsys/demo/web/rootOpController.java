package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import com.recomsys.demo.ml.JobRec;
import com.recomsys.demo.ml.SkillRec;
import com.recomsys.demo.web.Entity.FileOp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * Root Operation Function
 * in Administer Page
 * */

@Controller
public class rootOpController {


    /**
     * Upload File Function
     * input file
     * return json
     * json format{
     *     msg:"",
     *     description:"",
     *     name_list:[],
     *     current:""
     * }
     * */
    @RequestMapping("/uploadfile")
    @ResponseBody
    public String uploadFile(@RequestParam(value = "file") MultipartFile file,
                             HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login

            // File Save
            if (fileService.saveFile(file)) {
                msg.put("msg", "success");
                msg.put("description", "Upload Success");

            } else {
                msg.put("msg", "exception");
                msg.put("description", "Save file error");
            }

            List<String> list_name = fileService.getFillList();

            msg.put("name_list", list_name);
            msg.put("current", fileService.dataname);

            return msg.toJSONString();


        } else {
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
            // return error json
        }

        return msg.toJSONString();
    }

    /**
     * Operate File Function
     * 1. delete File
     * input json
     * return json
     * json format{
     *     msg:"",
     *     description:""
     *     name_list:[],
     *     current:""
     * }
     * */
    @RequestMapping("/operatefile")
    @ResponseBody
    public String adminFile(@RequestBody FileOp fileOp,
                            HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login

            if (fileOp.getState() == "2") {
                // File delete
                if (fileService.deleteFile(fileOp.getFilename())) {
                    msg.put("msg", "success");
                    msg.put("description", "delete Success");

                } else {
                    msg.put("msg", "exception");
                    msg.put("description", "file not found or delete error");
                }

                List<String> list_name = fileService.getFillList();

                msg.put("name_list", list_name);
                msg.put("current", fileService.dataname);
            } else {
                msg.put("msg", "error");
                msg.put("description", "unknow error");

            }
            return msg.toJSONString();

        } else {
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
            // return error json
        }

        return msg.toJSONString();
    }


    /**
     * Training Model Function
     * input: json
     * return json
     * json format{
     *     msg:"",
     *     description:""
     * }
     * */
    @RequestMapping("/train")
    @ResponseBody
    public String trainmodle(@RequestBody FileOp fileOp, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login

            // Spark API

            if (fileService.chooseFile(fileOp.getFilename())) {

                JobRec jobrec = new JobRec();

                boolean job1 = jobrec.chgTrainingData();

//                JobRec.sc.close();

                SkillRec skillrec = new SkillRec();

                boolean skill1 = skillrec.chgTrainingData();

//                SkillRec.sc.close();

                if (job1 && skill1) {
                    msg.put("msg", "success");
                    msg.put("description", "model generation success");

                } else {
                    msg.put("msg", "error");
                    msg.put("description", "Train Error");

                }

            } else {
                msg.put("msg", "error");
                msg.put("description", "Training File Error");
            }

        } else {
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
            // return error json
        }

        return msg.toJSONString();
    }

    /**
     * Administer Page Initial
     * return file list
     * input: null
     * return: json
     * json format{
     *     msg:"",
     *     description:""
     *     name_list:[]
     * }
     * */
    @RequestMapping("/adinitial")
    @ResponseBody
    public String uploadFile(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login
            msg.put("msg", "success");
            msg.put("description", "delete Success");
            List<String> list_name = fileService.getFillList();

            msg.put("name_list", list_name);
        }else{
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
        }
        return msg.toJSONString();
    }
}
