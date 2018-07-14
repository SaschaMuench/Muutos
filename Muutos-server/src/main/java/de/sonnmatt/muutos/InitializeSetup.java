package de.sonnmatt.muutos;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sonnmatt.muutos.jpa.LanguageJPA;
import de.sonnmatt.muutos.jpa.TenantJPA;
import de.sonnmatt.muutos.jpa.TextJPA;
import de.sonnmatt.muutos.jpa.TextSectionsExt;
import de.sonnmatt.muutos.jpa.UserJPA;
import static de.sonnmatt.muutos.jpa.TenantJPA.*;

public class InitializeSetup {
	private static Logger log = LogManager.getLogger(InitializeSetup.class);
	private static String classname = "InitializeSetup";
	private static EntityManager entityManager = HibernateUtil.getEntityManager();

	private LoadXML4Setup loadSystemXml;
	private LoadXML4Setup loadTranslationXml;

	public void setupSystem() {
		log.traceEntry("{} setupSystem()", classname);

		loadSystemXml = new LoadXML4Setup();
		loadSystemXml.loadXmlFile("SystemSetup.xml");

		loadTranslationXml = new LoadXML4Setup();
		loadTranslationXml.loadXmlFile("Translations.xml");

		languages();
		tenant();
		translations();
		saveSuperUser();

		log.traceExit("{} setupSystem()", classname);
	}

	private void tenant() {
		log.traceEntry("{}.tenant()", classname);

		entityManager.getTransaction().begin();

		HashMap<Integer, HashMap<String, String>> nodeData = new HashMap<>();
		nodeData = loadSystemXml.getMultiNodeData("/Setup/Tenants/Tenant");
		log.trace("/Setup/Tenants/Tenant[Count]: {}", nodeData.size());
		for (int temp = 0; temp < nodeData.size(); temp++) {
			HashMap<String, String> subNodeData = nodeData.get(temp);
			log.trace("{}.tenant(): generate tenant: {}", classname, subNodeData.get("Code"));
			TenantJPA tenant = new TenantJPA();
			tenant.setCode(subNodeData.get("Code"));
			tenant.setName(subNodeData.get("Name"));
			tenant.setUrlPart(subNodeData.get("UrlPart"));
			tenant.setType(subNodeData.get("Type"));
			tenant.setIsActive(Boolean.valueOf(subNodeData.get("IsActive")));
			tenant.setIsTest(Boolean.valueOf(subNodeData.get("IsTest")));
			tenant.setLevel(Integer.valueOf(subNodeData.get("Layer")));
			TenantJPA parentTenant = null;
			if (!(subNodeData.get("Parent") == null || subNodeData.get("Parent").isEmpty())) {
				List<TenantJPA> tenants = null;
				tenants = entityManager	.createNamedQuery(GetTenantByCode_OrderLevel, TenantJPA.class)
										.setParameter(ParamTenantCode, subNodeData.get("Parent"))
										.getResultList();
				log.trace("{}.tenant(): get parents({}): {}", classname, subNodeData.get("Code"), tenants.size());
				if (!tenants.isEmpty()) {
					parentTenant = tenants.get(0);
				}
			}
			tenant.setParent(parentTenant);
			entityManager.persist(tenant);
		}

		entityManager.flush();
		entityManager.getTransaction().commit();

		log.traceExit("{}.tenant()", classname);
	}

	private void languages() {
		log.traceEntry("{}.languages()", classname);

		entityManager.getTransaction().begin();

		HashMap<Integer, HashMap<String, String>> nodeData = new HashMap<>();
		nodeData = loadSystemXml.getMultiNodeData("/Setup/Languages/Language");
		log.trace("/Setup/Languages/Language[Count]: {}", nodeData.size());
		for (int temp = 0; temp < nodeData.size(); temp++) {
			HashMap<String, String> subNodeData = nodeData.get(temp);
			log.trace("{}.tenant(): generate language: {}", classname, subNodeData.get("Code"));
			entityManager.persist(new LanguageJPA(subNodeData.get("Code"), subNodeData.get("Description")));
		}
		entityManager.flush();
		entityManager.getTransaction().commit();

		log.traceExit("{}.languages()", classname);
	}

	private void translations() {
		log.traceEntry("{}.translations()", classname);

		TenantJPA tenant = null;
		List<TenantJPA> tenants = entityManager.createNamedQuery(GetTenantTop, TenantJPA.class).getResultList();
		if (!tenants.isEmpty()) {
			tenant = tenants.get(0);
		}
		List<LanguageJPA> languages = entityManager	.createNamedQuery(LanguageJPA.GetLanAll, LanguageJPA.class)
													.getResultList();

		log.trace("{}.translations(): tenant for tranlations: {}", classname, tenant.getCode());
		entityManager.getTransaction().begin();
		for (String action : TextSectionsExt.values()) {
			for (LanguageJPA lan : languages) {
				// "/Translations/Package[translate(@name,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')='%s'
				// and @language='%s']",
				String xPath = String.format("/Translations/Package[@name='%s' and @language='%s']", action,
						lan.getCode());
				log.trace("{}.translations(): xPath: {}", classname, xPath);
				HashMap<String, String> nodeData = new HashMap<>();
				nodeData = loadTranslationXml.getNodeAttributeData(xPath, "name");
				if ((nodeData != null) && (nodeData.size() > 0)) {
					log.trace("{}.translations(): nodes: {}", classname, nodeData.size());
					for (Entry<String, String> pair : nodeData.entrySet()) {
						log.trace("{}.translations(): generate: {}={}", classname, pair.getKey(), pair.getValue());
						entityManager.persist(
								new TextJPA(tenant, action + pair.getKey(), lan.getCode(), pair.getValue()));
					}
				} else {
					log.trace("{}.translations(): nodes: null or 0", classname);
				}
			}
		}
		entityManager.flush();
		entityManager.getTransaction().commit();

		log.traceExit("{}.translations()", classname);
	}

	private void saveSuperUser() {
		log.traceEntry("{} saveSuperUser()", classname);

		HashMap<String, String> userData = loadSystemXml.getNodeData("/Setup/User");
		UserJPA user = new UserJPA();
		user.setLoginName(userData.get("LoginName"));
		user.setPassword(userData.get("Password"));
		user.setPwdMustChange(Boolean.valueOf(userData.get("PwdMustChange")));
		user.setActive(Boolean.valueOf(userData.get("IsActive")));
		user.setLanCode(userData.get("LanCode"));
		user.setCompany(userData.get("Company"));
		user.setFirstName(userData.get("FirstName"));
		user.setLastName(userData.get("LastName"));
		user.setStreet(userData.get("Street"));
		user.setCity(userData.get("City"));
		user.setCountry(userData.get("Country"));
		user.setPhone(userData.get("Phone"));
		user.setMobile(userData.get("Mobile"));
		user.seteMail(userData.get("eMail"));

		TenantJPA tenant = null;
		List<TenantJPA> tenants = entityManager.createNamedQuery(GetTenantTop, TenantJPA.class).getResultList();
		if (!tenants.isEmpty()) {
			tenant = tenants.get(0);
		}
		user.setHomeTenant(tenant.getId());

		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.flush();
		entityManager.getTransaction().commit();

		log.traceExit("{} saveSuperUser()", classname);
	}
}