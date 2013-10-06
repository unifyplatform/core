package se.unify;

import java.sql.Date;

import org.apache.camel.language.XPath;

public class DbServiceBean {

    public String toSql(@XPath("invoice/@id") int id, 
            @XPath("invoice/date/text()") String date,
            @XPath("invoice/sum/text()") int sum) {

        /*
         *  id time_occurred status_code sum 
         */
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO INVOICE (id, time_occurred, status_code, sum) VALUES (");
        sb.append("'").append(id).append("', ");
        sb.append("'").append(date).append("', ");
        sb.append("'").append("1").append("', ");
        sb.append("'").append(sum).append("') ");

        System.out.println(" **** toSql returning: " + sb.toString());
        
        return sb.toString();
    }

    public String beanToSql(Invoice invoice) {

        /*
         *  id time_occurred status_code sum 
         */
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO INVOICE (id, time_occurred, status_code, sum) VALUES (");
        sb.append("'").append(invoice.id).append("', ");
        sb.append("'").append(new Date(invoice.date.getTime())).append("', ");
        sb.append("'").append("1").append("', ");
        sb.append("'").append(invoice.sum).append("') ");

        System.out.println(" **** beanToSql returning: " + sb.toString());

        return sb.toString();
    }

}
