package aiss.GitLabMiner.Auxiliary;

import org.springframework.http.ResponseEntity;

public class Pagination {

    public static Boolean hasMorePages (ResponseEntity response){

        String linkHeader = response.getHeaders().getFirst("Link");
        System.out.println(linkHeader);

        if (linkHeader == null || !linkHeader.contains("rel=\"next\"")) {
            return false;
        } else {
            return true;
        }

    }

}
