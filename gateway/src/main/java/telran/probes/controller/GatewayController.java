package telran.probes.controller;

import java.lang.ModuleLayer.Controller;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.service.GatewayService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GatewayController {
	
	final GatewayService gatewayService;
	
	@GetMapping("/**")
	ResponseEntity<byte[]> getProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		log.debug("Controller in GetMapping got request: {}", request.getRequestURL());
		return gatewayService.proxyRouting(exchange, request, "GET");
	}
	@PutMapping("/**")
	ResponseEntity<byte[]> putProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		log.debug("Controller in PutMapping got request: {}", request.getRequestURL());
		return gatewayService.proxyRouting(exchange, request, "PUT");
	}
	@PostMapping("/**")
	ResponseEntity<byte[]> postProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		log.debug("Controller in PostMapping got request: {}", request.getRequestURL());
		return gatewayService.proxyRouting(exchange, request, "POST");
	}
	@DeleteMapping("/**")
	ResponseEntity<byte[]> deleteProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		log.debug("Controller in DeleteMapping got request: {}", request.getRequestURL());
		return gatewayService.proxyRouting(exchange, request, "DELETE");
	}

}
