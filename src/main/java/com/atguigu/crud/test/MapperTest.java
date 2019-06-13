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
 * ����dao��Ĺ���
 * �Ƽ�spring����Ŀʹ��spring�ĵ�Ԫ����,�����Զ�ע��������Ҫ�����
 * 1������springtest��ģ��
 * 2��ʹ��@ContextConfigurationָ��spring�����ļ���λ��
 * 3��ֱ��autowiredҪʹ�õ��������
 */
@RunWith(SpringJUnit4ClassRunner.class)//��˭�ĵ�Ԫ����
@ContextConfiguration(locations={"classpath:springmvc.xml"})
public class MapperTest {
	
	@Autowired
	DepartmentMapper departmentMapper;
	
	@Autowired
	EmployeeMapper employeeMapper;//�Զ�ע��employeeMapper
	
	@Autowired
	SqlSession sqlSession;//�Զ�ע��sqlSession
	
	/*
	 * ����DepartmentMapper
	 */
	@Test
	public void testCRUD(){
		/*//1������SpringIOC����
		ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
		//2���������л�ȡmapper
		DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);*/
		
		System.out.println(departmentMapper);
		//1:���뼸������
		/*departmentMapper.insertSelective(new Department(null,"������"));	
		departmentMapper.insertSelective(new Department(null,"test"));*/	
		
		//2������Ա������ ����Ա������
		//employeeMapper.insertSelective(new Employee(null,"mike","M","jerry@atguigu",1));
	 
		//3������������Ա��  ʹ�ÿ���ִ������������sqlSession
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
	    for(int i = 0;i<10;i++){
	    	String uid = UUID.randomUUID().toString().substring(0,5) + i;//��ȡuuid��һ����
	    	mapper.insertSelective(new Employee(null,uid,"M",uid + "@atguigu.com",1));
	    }
	}

}
