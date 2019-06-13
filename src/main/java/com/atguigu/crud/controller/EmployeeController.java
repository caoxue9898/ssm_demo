package com.atguigu.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.Msg;
import com.atguigu.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/*
 * ����Ա����ɾ�ò�����
 */
@Controller
public class EmployeeController {

	@Autowired
	// �Զ�װ��service��ҵ���߼����
	EmployeeService employeeService;

	/*
	 * ������������һ ����ɾ��:1-2-3 ����ɾ��:1
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{ids}", method = RequestMethod.DELETE)
	// ����������id��emp ������·���д�id
	public Msg deleteEmpById(@PathVariable("ids") String ids) {// ������·���л�ȡid
		if (ids.contains("-")) {
			// ���ɾ�������
			List<Integer> del_ids = new ArrayList();
			String[] str_ids = ids.split("-");
			// ��װid�ļ���

			for (String string : str_ids) {
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		} else {
			// ����ɾ�������
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmp(id);
		}
		return Msg.success();
	}

	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(value =
	 * "/emp/{id}",method=RequestMethod.DELETE)//����������id��emp ������·���д�id public
	 * Msg deleteEmpById(@PathVariable("id")Integer id){//������·���л�ȡid
	 * employeeService.deleteEmp(id); return Msg.success(); }
	 */

	/*
	 * ��֧��ֱ�ӷ���PUT֮�������Ҫ��װ�������е����� ����HttpPutFormContentFilter������ ����
	 * ���������е����ݰ�װ��һ��map request�����°�װ request.getParameter()����д �ͻ���Լ���װ��map��ȡ����
	 * Ա������
	 */
	@ResponseBody
	@RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
	public Msg saveEmp(Employee employee) {
		employeeService.updateEmp(employee);
		return Msg.success();
	}

	/*
	 * ����id��ѯ
	 */
	@RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);

	}

	/*
	 * ����û����Ƿ����
	 */
	@ResponseBody
	@RequestMapping("/checkUser")
	public Msg checkUser(@RequestParam("empName") String empName) {// ʹ��ע��@RequestParam��ȷ�ĸ���springmvcҪȡ��empName��ֵ
		// ���ж��û����Ƿ��ǺϷ��ı��ʽ
		String regex = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\u2E80-\u9FFF]{2,5})";
		if (!empName.matches(regex)) {
			return Msg.fail().add("va_msg",
					"(���Ժ�˵���Ϣ)�û���������6-16λ���ֺ���ĸ����ϻ���2-5λ������");// ״̬200
		}

		// ���ݿ��û����ظ�У��
		boolean b = employeeService.checkUser(empName);
		if (b) {
			return Msg.success();// ״̬��100//�ͻ���ͨ����״̬���ʶ�����ж��Ƿ����
		} else {
			return Msg.fail().add("va_msg", "�û���������");// ״̬200
		}
	}

	/*
	 * ����Ա������
	 */
	@RequestMapping(value = "/emp", method = RequestMethod.POST)
	// REST����uri
	@ResponseBody
	// BindingResult ��װУ����
	// ʵ���Զ���װ // @Valid �����װ��������Ժ�����������������Ҫ����У��
	public Msg saveEmp(@Valid Employee employee, BindingResult result) {
		if (result.hasErrors()) {
			Map<String, Object> map = new HashMap();
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println("������ֶ���:" + fieldError.getField());
				System.out.println("������Ϣ:" + fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);// У��ʧ�ܣ�Ӧ�÷���ʧ�ܣ���ģ̬���з���У��ʧ�ܵĴ�����Ϣ
		} else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}

	/*
	 * @ResponseBody��Ҫ��������Ӧ����İ��� ���𽫶���ת��Ϊjson�ַ��� ���İ� ע��� ��̬��
	 */
	// �˷�����Ա���ķ�ҳ��ѯ����Ϊajax���� ����json���� ���������ҳ��Ϣ��Ա����Ϣ
	@RequestMapping("/emps")
	// ����������յ�json���ص��ַ���
	@ResponseBody
	// ��ע����԰ѷ��صĶ���תΪjson�ַ���
	// ҳ�淢�������ʱ�������Ҫ��ҳ������
	public Msg getEmpsWithJson(
			@RequestParam(value = "pn", defaultValue = "1") Integer pn,
			Model model) {
		// ����PageHelper��ҳ
		// �ڲ�ѯ֮ǰֻ��Ҫ����,����ҳ�룬�Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		// startPage��������Ĳ�ѯ����һ����ҳ��ѯ

		List<Employee> emps = employeeService.getAll();// ��ѯ����Ա������-->��ҳ��ѯ
		// ʹ��PageInfo��װ��ѯ��Ľ��,ֻ��Ҫ��PageInfo����ҳ��
		// PageInfo�ַ�װ����ϸ�ķ�ҳ��Ϣ �������ǲ�ѯ����������
		PageInfo page = new PageInfo(emps, 5);// 5Ϊ������ʾ��ҳ��
		return Msg.success().add("pageInfo", page);// ֮ǰ��Ҫ���������request���У�����ֱ�ӷ��ؼ���
	}

	/*
	 * ��ѯԱ������(��ҳ��ѯ)
	 */
	// @RequestMapping("/emps")//���������
	public String getEmps(
			@RequestParam(value = "pn", defaultValue = "1") Integer pn,
			Model model) {
		// ����PageHelper��ҳ
		// �ڲ�ѯ֮ǰֻ��Ҫ����,����ҳ�룬�Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		// startPage��������Ĳ�ѯ����һ����ҳ��ѯ

		List<Employee> emps = employeeService.getAll();// ��ѯ����Ա������-->��ҳ��ѯ
		// ʹ��PageInfo��װ��ѯ��Ľ��,ֻ��Ҫ��PageInfo����ҳ��
		// PageInfo�ַ�װ����ϸ�ķ�ҳ��Ϣ �������ǲ�ѯ����������
		PageInfo page = new PageInfo(emps, 5);// 5Ϊ������ʾ��ҳ��
		model.addAttribute("pageInfo", page);
		// page.getNavigatepageNums();//�õ�������ʾ��ҳ��
		return "list";// ͨ����ͼ��������listҳ�����չʾ
	}

}
