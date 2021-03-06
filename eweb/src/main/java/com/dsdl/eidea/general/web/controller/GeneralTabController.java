package com.dsdl.eidea.general.web.controller;

import com.dsdl.eidea.base.entity.bo.FieldInListPageBo;
import com.dsdl.eidea.base.entity.bo.SelectItemBo;
import com.dsdl.eidea.base.entity.bo.UserBo;
import com.dsdl.eidea.base.service.FieldService;
import com.dsdl.eidea.base.service.TabService;
import com.dsdl.eidea.core.dto.PaginationResult;
import com.dsdl.eidea.core.params.DeleteParams;
import com.dsdl.eidea.core.params.QueryParams;
import com.dsdl.eidea.core.web.def.WebConst;
import com.dsdl.eidea.core.web.result.JsonResult;
import com.dsdl.eidea.core.web.util.WebUtil;
import com.dsdl.eidea.general.bo.FieldStructureBo;
import com.dsdl.eidea.general.bo.TabFormStructureBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 刘大磊 on 2017/5/24 11:08.
 */
@Controller
@RequestMapping("/general/tab")
public class GeneralTabController {
    @Autowired
    private TabService tabService;
    @Autowired
    private FieldService fieldService;

    /**
     * 显示tab列表界面
     *
     * @param tabId
     * @return
     */
    @RequestMapping("/showList/{tabId}")
    public ModelAndView showListPage(@PathVariable("tabId") Integer tabId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("general/list");
        String lang = (String) session.getAttribute(WebConst.SESSION_LANGUAGE);
        List<FieldInListPageBo> fieldInListPageBoList = fieldService.getListPageFiledList(tabId, lang);
        modelAndView.addObject("fieldInListPageBoList", fieldInListPageBoList);
        modelAndView.addObject("tabId", tabId);
        Integer pkFieldId = tabService.getTabPkFieldId(tabId);
        modelAndView.addObject("pk", "id" + pkFieldId);
        return modelAndView;
    }

    @RequestMapping("/list/{tabId}")
    @ResponseBody
    public JsonResult<PaginationResult<Map<String, Object>>> list(@PathVariable("tabId") Integer tabId, @RequestBody QueryParams queryParams) {
        PaginationResult list = fieldService.getDataList(tabId, queryParams);
        return JsonResult.success(list);
    }

    /**
     * 显示tab编辑界面
     *
     * @param tabId
     * @return
     */
    @RequestMapping("/showForm/{tabId}")
    public ModelAndView showFormPage(@PathVariable("tabId") Integer tabId, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("general/edit");
        String lang = (String) session.getAttribute(WebConst.SESSION_LANGUAGE);
        TabFormStructureBo tabFormStructureBo = fieldService.getFormPageFieldList(tabId, lang);
        modelAndView.addObject("tabId", tabId);
        modelAndView.addObject("tabFormStructureBo", tabFormStructureBo);
        return modelAndView;
    }

    @RequestMapping("/create/{tabId}")
    @ResponseBody
    public JsonResult<Map<String, Object>> create(@PathVariable("tabId") Integer tabId, HttpSession session) {
        String lang = (String) session.getAttribute(WebConst.SESSION_LANGUAGE);
        UserBo userBo = WebUtil.getUserBoInSession(session);
        Map<String, Object> result = fieldService.getNewObject(tabId, lang, userBo);
        return JsonResult.success(result);
    }

    @RequestMapping("/get/{tabId}/{recordId}")
    @ResponseBody
    public JsonResult<Map<String, Object>> edit(@PathVariable("tabId") Integer tabId, @PathVariable("recordId") Integer recordId) {
        Map<String, Object> result = fieldService.getDataForm(tabId, recordId);
        return JsonResult.success(result);
    }

    @RequestMapping("/getSelectList/{fieldId}")
    @ResponseBody
    public JsonResult<List<SelectItemBo>> getSelectList(@PathVariable("fieldId") Integer fieldId) {
        List<SelectItemBo> selectItemBoList = fieldService.getSelectItemList(fieldId);
        return JsonResult.success(selectItemBoList);
    }

    /**
     * 保存更新
     *
     * @param tabId
     * @param model
     * @return
     */
    @RequestMapping("/saveForUpdated/{tabId}")
    @ResponseBody
    public JsonResult<Map<String, Object>> saveForUpdated(@PathVariable("tabId") Integer tabId, @RequestBody Map<String, Object> model, HttpServletRequest request) {
        UserBo userBo = WebUtil.getUserBoInSession(request);
        Map<String, Object> result = fieldService.saveForUpdated(tabId, model, userBo);
        return JsonResult.success(result);
    }

    /**
     * @param tabId
     * @param model
     * @return
     */
    @RequestMapping("/saveForCreated/{tabId}")
    @ResponseBody
    public JsonResult<Map<String, Object>> saveForCreated(@PathVariable("tabId") Integer tabId, @RequestBody Map<String, Object> model, HttpServletRequest request) {
        UserBo userBo = WebUtil.getUserBoInSession(request);
        Map<String, Object> result = fieldService.saveForCreated(tabId, model, userBo);
        return JsonResult.success(result);
    }

    /**
     * @param tabId
     * @return
     */
    @RequestMapping("/deletes/{tabId}")
    @ResponseBody
    public JsonResult<PaginationResult<Map<String, Object>>> deletes(@PathVariable("tabId") Integer tabId, @RequestBody DeleteParams<Object> deleteParams) {
        fieldService.deleteList(tabId, deleteParams.getIds());
        return list(tabId, deleteParams.getQueryParams());
    }
}

