package bolivariano.offline.reentry.repo;
import java.util.Calendar;

import javax.persistence.Entity;

@Entity
public class LogReentry {

	private Calendar re_fecha;
	private String re_canal;
	private String re_request;
	private String re_response;
	private String re_estado;
	private String re_codigo;
	private String re_mensaje;
	
	public Calendar getRe_fecha() {
		return re_fecha;
	}
	public void setRe_fecha(Calendar re_fecha) {
		this.re_fecha = re_fecha;
	}
	public String getRe_canal() {
		return re_canal;
	}
	public void setRe_canal(String re_canal) {
		this.re_canal = re_canal;
	}
	public String getRe_request() {
		return re_request;
	}
	public void setRe_request(String re_request) {
		this.re_request = re_request;
	}
	public String getRe_response() {
		return re_response;
	}
	public void setRe_response(String re_response) {
		this.re_response = re_response;
	}
	public String getRe_estado() {
		return re_estado;
	}
	public void setRe_estado(String re_estado) {
		this.re_estado = re_estado;
	}
	public String getRe_codigo() {
		return re_codigo;
	}
	public void setRe_codigo(String re_codigo) {
		this.re_codigo = re_codigo;
	}
	public String getRe_mensaje() {
		return re_mensaje;
	}
	public void setRe_mensaje(String re_mensaje) {
		this.re_mensaje = re_mensaje;
	}
	
}
