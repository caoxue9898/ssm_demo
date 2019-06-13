package com.atguigu.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.EmployeeExample;
import com.atguigu.crud.bean.EmployeeExample.Criteria;
import com.atguigu.crud.dao.EmployeeMapper;

@Service//sevice��һ��ҵ���߼����
public class EmployeeService {
	
	@Autowired
	EmployeeMapper employeeMapper;

	/*
	 * ��ѯ����Ա��
	 */
	public List<Employee> getAll() {
		return employeeMapper.selectByExampleWithDept(null);//��ѯ���У�û������
	}

	/*
	 * Ա������
	 */
	public void saveEmp(Employee employee) {
		employeeMapper.insertSelective(employee);//��ѡ��Ĳ���  id����  ������
	}

	/*
	 * �����û����Ƿ����
	 * true:����ǰ��������
	 * false:������
	 */
	public boolean checkUser(String empName) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();//�����ѯ����
		criteria.andEmpNameEqualTo(empName);
		long count = employeeMapper.countByExample(example);//��������ͳ�Ʒ��������ļ�¼��
		return count==0;
	}

	/*
	 * ����Ա��id��ѯԱ��
	 */
	public Employee getEmp(Integer id) {
		Employee employee = employeeMapper.selectByPrimaryKey(id);
		return employee;
	}

	/*
	 * Ա������
	 */
	public void updateEmp(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);
	}

	/*
	 * Ա��ɾ��
	 */
	public void deleteEmp(Integer id) {
		employeeMapper.deleteByPrimaryKey(id);
	}

	public void deleteBatch(List<Integer> ids) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();
		//delete from xxx from emp_id in(1,2,3)
		criteria.andEmpIdIn(ids);
		employeeMapper.deleteByExample(example);
	}

	

}
