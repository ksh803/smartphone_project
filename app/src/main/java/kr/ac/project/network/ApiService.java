package kr.ac.project.network;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

import kr.ac.project.Fragment.Company;

public interface ApiService {
    @GET("companies") // 엔드포인트 지정
    Call<List<Company>> getCompanies();
}
