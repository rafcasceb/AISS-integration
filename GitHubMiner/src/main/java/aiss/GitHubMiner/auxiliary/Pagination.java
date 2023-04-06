package aiss.GitHubMiner.auxiliary;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
