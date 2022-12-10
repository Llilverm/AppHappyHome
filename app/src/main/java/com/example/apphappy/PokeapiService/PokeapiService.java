package com.example.apphappy.PokeapiService;

import com.example.apphappy.models.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeapiService {

    @GET("pokemon")                               //obtener la informacion de la url
    Call<PokemonRespuesta> obtenerListaPokemon(@Query("limit")int limit,@Query("offset")int offset);

}
