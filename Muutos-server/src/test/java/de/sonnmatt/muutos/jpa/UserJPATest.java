package de.sonnmatt.muutos.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;

import de.sonnmatt.muutos.HibernateUtil;

class UserJPATest {

	@Test
	void testUserJPA() {
		EntityManager entityManager = HibernateUtil.getEntityManager();
		List<UserJPA> user = entityManager	.createNamedQuery(UserJPA.GetUserByLogin, UserJPA.class)
											.setParameter("login", "smu")
											.getResultList();
		assertEquals(1, user.size(), "Size != 1");
		assertEquals("be42131c-f2bb-4fe3-b726-190bb3062b94", user.get(0).getHomeTenant(), "HomeTenant not correct");
	}

}
