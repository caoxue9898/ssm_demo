package com.atguigu.crud.test;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.atguigu.crud.bean.Department;
import com.atguigu.crud.bean.Employee;
import com.atguigu.crud.dao.DepartmentMapper;
import com.atguigu.crud.dao.EmployeeMapper;

/*
 * 测试dao层的功能
 * 推荐spring的项目使用spring的单元测试,可以自动注入我们需要的组件
 * 1：导入springtest的模块
 * 2：使用@ContextConfiguration指定spring配置文件的位置
 * 3：直接autowired要使用的组件即可
 */
@RunWith(SpringJUnit4ClassRunner.class)//用谁的单元测试
@ContextConfiguration(locations={"classpath:springmvc.xml"})
public class MapperTest {
	
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;//自动注入employeeMapper
	
	@Autowired
	SqlSession sqlSession;//自动注入sqlSession
	
	/*
	 * 测试DepartmentMapper
	 */
	@Test
	public void testCRUD(){
		/*//1：创建SpringIOC容器
		ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
		//2：从容器中获取mapper
		DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);*/
		
		System.out.println(departmentMapper);
		//1:插入几个部门
		/*departmentMapper.insertSelective(new Department(null,"开发部"));	
		departmentMapper.insertSelective(new Department(null,"test"));*/	
		
		//2：生成员工数据 测试员工插入
		//employeeMapper.insertSelective(new Employee(null,"mike","M","jerry@atguigu",1));
	 
		//3：批量插入多个员工  使用可以执行批量操作的sqlSession
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
	    for(int i = 0;i<10;i++){
	    	String uid = UUID.randomUUID().toString().substring(0,5) + i;//截取uuid的一部分
	    	mapper.insertSelective(new Employee(null,uid,"M",uid + "@atguigu.com",1));
	    }
	}

}
