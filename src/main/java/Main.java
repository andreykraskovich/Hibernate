import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.sql.*;
import java.util.List;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/skillbox";
    private static final String NAME = "root";
    private static final String PASSWORD = "";
    private static final String SQL =
            "SELECT course_name, COUNT(*) / (MAX(month(subscription_date)) - MIN(month(subscription_date)) + 1) AS avg "
                    + "FROM PurchaseList "
                    + "WHERE YEAR (subscription_date) = ? "
                    + "GROUP BY course_name;";

    public static void main(String[] args) {
        try{
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        List<PurchaseList> purchaseList = session.createQuery("from PurchaseList")
                .getResultList();
        for (PurchaseList var : purchaseList) {

            DetachedCriteria studentsCriteria = DetachedCriteria.forClass(Students.class)
                    .add(Restrictions.eq("name", var.getStudentName()));
            Students student = (Students) studentsCriteria.getExecutableCriteria(session).list().stream()
                    .findFirst().get();

            DetachedCriteria coursesCriteria = DetachedCriteria.forClass(Course.class)
                    .add(Restrictions.eq("name", var.getCourseName()));
            Course course = (Course) coursesCriteria.getExecutableCriteria(session).list().stream()
                    .findFirst().get();
            System.out.println(course.getName());

            LinkedPurchaseList linkedPurchaseList = new LinkedPurchaseList(
                    new LinkedPurchaseList.Id(student.getId(), course.getId()), student, course,
                    course.getPrice(), var.getSubscriptionDate());
            session.save(linkedPurchaseList);

        }
            transaction.commit();
    }catch (Exception exception){
            exception.printStackTrace();
        }
}
}
