package com.atguigu.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.bean.EmployeeExample;
import com.atguigu.crud.bean.EmployeeExample.Criteria;
import com.atguigu.crud.dao.EmployeeMapper;

@Service//sevice是一个业务逻辑组件
public class EmployeeService {
	
	@Autowired
	EmployeeMapper employeeMapper;

	/*
	 * 查询所有员工
	 */
	public List<Employee> getAll() {
		return employeeMapper.selectByExampleWithDept(null);//查询所有，没有条件
	}

	/*
	 * 员工保存
	 */
	public void saveEmp(Employee employee) {
		employeeMapper.insertSelective(employee);//有选择的插入  id自增  不插入
	}

	/*
	 * 检验用户名是否可用
	 * true:代表当前姓名可用
	 * false:不可用
	 */
	public boolean checkUser(String empName) {
		EmployeeExample example = new EmployeeExample();
		Criteria criteria = example.createCriteria();//构造查询条件
		criteria.andEmpNameEqualTo(empName);
		long count = employeeMapper.countByExample(example);//按照条件统计符合条件的记录数
		return count==0;
	}

	/*
	 * 按照员工id查询员工
	 */
	public Employee getEmp(Integer id) {
		Employee employee = employeeMapper.selectByPrimaryKey(id);
		return employee;
	}

	/*
	 * 员工更新
	 */
	public void updateEmp(Employee employee) {
		employeeMapper.updateByPrimaryKeySelective(employee);
	}

	/*
	 * 员工删除
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
