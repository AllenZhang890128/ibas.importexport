package org.colorcoding.ibas.importexport.test.transformers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.colorcoding.ibas.bobas.serialization.ISerializer;
import org.colorcoding.ibas.bobas.serialization.SerializerFactory;
import org.colorcoding.ibas.importexport.MyConfiguration;
import org.colorcoding.ibas.importexport.transformers.ITransformer;
import org.colorcoding.ibas.importexport.transformers.JsonTransformer;
import org.colorcoding.ibas.importexport.transformers.TransformException;
import org.colorcoding.ibas.importexport.transformers.XmlTransformer;
import org.colorcoding.ibas.initialfantasy.bo.organizations.IOrganizationalRole;
import org.colorcoding.ibas.initialfantasy.bo.organizations.IOrganizationalStructure;
import org.colorcoding.ibas.initialfantasy.bo.organizations.IRoleMember;
import org.colorcoding.ibas.initialfantasy.bo.organizations.OrganizationalRole;
import org.colorcoding.ibas.initialfantasy.bo.organizations.OrganizationalStructure;
import org.colorcoding.ibas.initialfantasy.bo.organizations.RoleMember;

import junit.framework.TestCase;

public class testTransformer extends TestCase {

	private String createFileData(String type) throws IOException {
		IOrganizationalStructure organizational = new OrganizationalStructure();
		organizational.setObjectKey((int) System.currentTimeMillis());
		organizational.setManager("jobs");
		IOrganizationalRole organizationalRole = organizational.getOrganizationalRoles().create();
		organizationalRole.setRole("boss");
		IRoleMember roleMember = organizationalRole.getRoleMembers().create();
		roleMember.setMember("Mr Zhang San");
		roleMember = organizationalRole.getRoleMembers().create();
		roleMember.setMember("Ms Li Si");
		organizationalRole = organizational.getOrganizationalRoles().create();
		organizationalRole.setRole("thief");
		roleMember = organizationalRole.getRoleMembers().create();
		roleMember.setMember("Ms Wang Wu");
		roleMember = organizationalRole.getRoleMembers().create();
		roleMember.setMember("Ms Zhao Liu");
		ISerializer<?> serializer = SerializerFactory.create().createManager().create(type);
		String filePath = String.format("%s%s~%s.%s", MyConfiguration.getDataFolder(), File.separator,
				UUID.randomUUID().toString(), type);
		File file = new File(filePath);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream outputStream = new FileOutputStream(file);
		serializer.serialize(organizational, outputStream);
		return file.getPath();
	}

	public void testJSON() throws TransformException, IOException {
		ITransformer transformer = new JsonTransformer();
		transformer.addKnownType(OrganizationalStructure.class);
		transformer.addKnownType(OrganizationalRole.class);
		transformer.addKnownType(RoleMember.class);
		// 测试转换BO
		transformer.setData(this.createFileData(JsonTransformer.TYPE_NAME));
		transformer.transform();
		for (Object item : transformer.getBOs()) {
			System.out.println(item.toString());
		}
		// 测试转换文件
		transformer.addBOs(transformer.getBOs());
		transformer.transform();
		for (Object item : (String[]) transformer.getData()) {
			System.out.println(item.toString());
		}
	}

	public void testXML() throws TransformException, IOException {
		ITransformer transformer = new XmlTransformer();
		transformer.addKnownType(OrganizationalStructure.class);
		transformer.addKnownType(OrganizationalRole.class);
		transformer.addKnownType(RoleMember.class);
		// 测试转换BO
		transformer.setData(this.createFileData(XmlTransformer.TYPE_NAME));
		transformer.transform();
		for (Object item : transformer.getBOs()) {
			System.out.println(item.toString());
		}
		// 测试转换文件
		transformer.addBOs(transformer.getBOs());
		transformer.transform();
		for (Object item : (String[]) transformer.getData()) {
			System.out.println(item.toString());
		}
	}
}
