package com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.Controller;

import com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.DTO.LoginRequest;
import com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.DTO.TokenResponse;
import com.digis01.LDBarajasProgramacionNCapasSeptiembre2025.ML.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import jakarta.servlet.http.HttpSession;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

//    private RestTemplate restTemplate;
    private final String URL = "http://localhost:8080/api/usuario";
    private final String AUTH_URL = "http://localhost:8080/api/auth/login";
//------------------------------------------------------LOGIN Y LOGOUT-----------------------------------------------------

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "usuarioInactivo", required = false) String usuarioInactivo,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("mensaje", "Usuario o contraseña incorrectos");
        }
        if (usuarioInactivo != null) {
            model.addAttribute("mensaje", "Usuario inactivo");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada correctamente");
        }
        return "UsuarioLogin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
            ResponseEntity<TokenResponse> response = restTemplate.exchange(
                    AUTH_URL,
                    HttpMethod.POST,
                    request,
                    TokenResponse.class
            );
            session.setAttribute("token", response.getBody().getToken());
            return "redirect:/usuario";

        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
//------------------------------------------------------USUARIO INDEX-----------------------------------------------------

    @GetMapping
    public String Index(Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login?expired=true";
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Result<Usuario>> responseUsuarios = restTemplate.exchange(
                    URL,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<Usuario>>() {
            }
            );
            ResponseEntity<Result<Rol>> responseRoles = restTemplate.exchange(
                    URL + "/roles",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<Rol>>() {
            }
            );
            if (responseUsuarios.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("usuarios", responseUsuarios.getBody().objects);
            } else {
                model.addAttribute("usuarios", null);
            }
            if (responseRoles.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("roles", responseRoles.getBody().objects);
            } else {
                model.addAttribute("roles", null);
            }
            model.addAttribute("Usuario", new Usuario());
            return "UsuarioIndex";
        } catch (Exception ex) {
            return "redirect:/login?forbidden=true";
        }
    }

//
//    @PostMapping("/procesar")
//    public String procesarUsuarios(HttpSession session, Model model) {
//
//        // Recuperar solo el nombre del archivo guardado en la carga
//        String nombreArchivo = (String) session.getAttribute("nombreArchivoTemp");
//
//        if (nombreArchivo == null || nombreArchivo.isBlank()) {
//            model.addAttribute("mensajeError", "No se encontró el archivo a procesar. Primero debe cargarlo.");
//            return "UsuarioCargaMasiva";
//        }
//
//        List<Usuario> usuarios = new ArrayList<>();
//
//        try {
//            String pathBase = System.getProperty("user.dir");
//            String pathArchivo = pathBase + "/src/main/resources/ArchivosCarga/" + nombreArchivo;
//            File archivo = new File(pathArchivo);
//
//            if (!archivo.exists()) {
//                model.addAttribute("mensajeError", "El archivo no existe en el servidor: " + nombreArchivo);
//                return "UsuarioCargaMasiva";
//            }
//
//            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
//
//            switch (extension) {
//                case "txt" ->
//                    usuarios = leerArchivoTXT(archivo);
//                case "xlsx" ->
//                    usuarios = leerArchivoXLSX(archivo);
//                default -> {
//                    model.addAttribute("mensajeError", "Extensión no soportada: " + extension);
//                    return "UsuarioCargaMasiva";
//                }
//            }
//
//            if (usuarios.isEmpty()) {
//                model.addAttribute("mensajeError", "El archivo no contiene usuarios válidos para insertar.");
//                return "UsuarioCargaMasiva";
//            }
//
//            // Insertar directamente
//            Result result = usuarioJPADAOImplementation.AddAll(usuarios);
//
//            if (result.correct) {
//                model.addAttribute("mensajeExito", "Usuarios procesados correctamente desde el archivo '" + nombreArchivo + "'.");
//            } else {
//                model.addAttribute("mensajeError", "Error al insertar los datos: " + result.errorMessage);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("mensajeError", "Error al procesar el archivo: " + e.getMessage());
//        }
//
//        // Limpiar la sesión (opcional)
//        session.removeAttribute("nombreArchivoTemp");
//
//        return "UsuarioCargaMasiva";
//    }
//
//    @PostMapping("/carga")
//    public String procesarCarga(@RequestParam("archivo") MultipartFile archivo, Model model, HttpSession session) throws IOException {
//
//        if (archivo == null || archivo.isEmpty()) {
//            model.addAttribute("mensajeError", "Debe seleccionar un archivo para cargar.");
//            return "UsuarioCargaMasiva";
//        }
//
//        String nombreArchivo = archivo.getOriginalFilename();
//        if (nombreArchivo == null) {
//            model.addAttribute("mensajeError", "El archivo cargado no es válido.");
//            return "UsuarioCargaMasiva";
//        }
//
//        String extension = nombreArchivo.contains(".")
//                ? nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1)
//                : "";
//
//        // Ruta destino
//        String pathBase = System.getProperty("user.dir");
//        String pathCarpeta = "src/main/resources/ArchivosCarga";
//        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        File carpetaDestino = new File(pathBase + "/" + pathCarpeta);
//        if (!carpetaDestino.exists()) {
//            carpetaDestino.mkdirs();
//        }
//
//        String nombreFinal = fecha + "_" + nombreArchivo;
//        String pathDefinitivo = pathBase + "/" + pathCarpeta + "/" + nombreFinal;
//
//        archivo.transferTo(new File(pathDefinitivo));
//
//        List<Usuario> usuarios = new ArrayList<>();
//        List<ErrorCarga> errores = new ArrayList<>();
//
//        switch (extension.toLowerCase()) {
//            case "txt" -> {
//                usuarios = leerArchivoTXT(new File(pathDefinitivo));
//                errores = validarDatosArchivo(usuarios);
//            }
//            case "xlsx" -> {
//                usuarios = leerArchivoXLSX(new File(pathDefinitivo));
//                errores = validarDatosArchivo(usuarios);
//            }
//            default -> {
//                model.addAttribute("mensajeError", "Extensión no soportada. Solo se permiten archivos .txt o .xlsx");
//                return "UsuarioCargaMasiva";
//            }
//        }
//
//        // Si hay errores: mostrar en vista, limpiar sesión
//        if (!errores.isEmpty()) {
//            session.removeAttribute("usuariosTemp");
//            session.removeAttribute("nombreArchivoTemp");
//
//            model.addAttribute("errores", errores);
//            model.addAttribute("mensajeError", "Se encontraron errores en la validación del archivo.");
//        } // Si todo está bien: guardar en sesión
//        else {
//            session.setAttribute("usuariosTemp", usuarios);
//            session.setAttribute("nombreArchivoTemp", nombreFinal);
//
//            model.addAttribute("usuarios", usuarios);
//            model.addAttribute("mensajeExito", "Archivo cargado correctamente. Puede procesar los datos.");
//        }
//
//        model.addAttribute("nombreArchivo", nombreFinal);
//        return "UsuarioCargaMasiva";
//    }
//
//    public List<ErrorCarga> validarDatosArchivo(List<Usuario> usuarios) {
//        List<ErrorCarga> erroresCarga = new ArrayList<>();
//        if (usuarios == null) {
//            return erroresCarga;
//        }
//
//        int lineaError = 0;
//        for (Usuario usuario : usuarios) {
//            lineaError++;
//            BindingResult bindingResult = validationService.validateObject(usuario);
//            List<ObjectError> errors = bindingResult.getAllErrors();
//
//            for (ObjectError error : errors) {
//                if (error instanceof FieldError fieldError) {
//                    ErrorCarga errorCarga = new ErrorCarga();
//                    errorCarga.setCampo(fieldError.getField());
//                    errorCarga.setDescripcion(fieldError.getDefaultMessage());
//                    errorCarga.setLinea(lineaError);
//                    erroresCarga.add(errorCarga);
//                }
//            }
//        }
//        return erroresCarga;
//    }
//
//    public List<Usuario> leerArchivoTXT(File archivo) {
//        List<Usuario> usuarios = new ArrayList<>();
//
//        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
//            String linea;
//            while ((linea = br.readLine()) != null) {
//                String[] campos = linea.split("\\|");
//
//                // Validar número de campos
//                if (campos.length < 12) {
//                    continue;
//                }
//
//                Usuario usuario = new Usuario();
//                usuario.setUserName(campos[0]);
//                usuario.setNombreUsuario(campos[1]);
//                usuario.setApellidoPat(campos[2]);
//                usuario.setApellidoMat(campos[3]);
//                usuario.setEmail(campos[4]);
//                usuario.setPassword(campos[5]);
//
//                // Parsear fecha
//                Date fechaFormateada = new SimpleDateFormat("yyyy/MM/dd").parse(campos[6]);
//                usuario.setFechaNacimiento(fechaFormateada);
//
//                usuario.setSexo(campos[7]);
//                usuario.setTelefono(campos[8]);
//                usuario.setCelular(campos[9]);
//                usuario.setCurp(campos[10]);
//
//                Rol rol = new Rol();
//                rol.setIdRol(Integer.parseInt(campos[11]));
//                usuario.setRol(rol);
//
//                usuarios.add(usuario);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return usuarios;
//    }
//
//    public List<Usuario> leerArchivoXLSX(File archivo) {
//        List<Usuario> usuarios = new ArrayList<>();
//
//        try (InputStream fileImputStream = new FileInputStream(archivo); XSSFWorkbook workbook = new XSSFWorkbook(fileImputStream)) {
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            boolean encabezadoDetectado = false;
//
//            int rowIndex = 0;
//            for (Row row : sheet) {
//                if (row == null) {
//                    rowIndex++;
//                    continue;
//                }
//                // Detectar si es encabezado (solo la primera fila)
//                if (rowIndex == 0 && esEncabezado(row)) {
//                    encabezadoDetectado = true;
//                    rowIndex++;
//                    continue;
//                }
//                Usuario usuario = new Usuario();
//
//                usuario.setUserName(getCellValue(row.getCell(0)));
//                usuario.setNombreUsuario(getCellValue(row.getCell(1)));
//                usuario.setApellidoPat(getCellValue(row.getCell(2)));
//                usuario.setApellidoMat(getCellValue(row.getCell(3)));
//                usuario.setEmail(getCellValue(row.getCell(4)));
//                usuario.setPassword(getCellValue(row.getCell(5)));
//
//                Cell cellFecha = row.getCell(6);
//                if (cellFecha != null) {
//                    if (DateUtil.isCellDateFormatted(cellFecha)) {
//                        usuario.setFechaNacimiento(cellFecha.getDateCellValue());
//                    } else {
//                        try {
//                            String fechaStr = getCellValue(cellFecha);
//                            if (!fechaStr.isBlank()) {
//                                LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                                usuario.setFechaNacimiento(java.sql.Date.valueOf(fecha));
//                            }
//                        } catch (Exception e) {
//                            usuario.setFechaNacimiento(null);
//                        }
//                    }
//                }
//
//                usuario.setSexo(getCellValue(row.getCell(7)));
//                usuario.setTelefono(getCellValue(row.getCell(8)));
//                usuario.setCelular(getCellValue(row.getCell(9)));
//                usuario.setCurp(getCellValue(row.getCell(10)));
//
//                Rol rol = new Rol();
//                String rolStr = getCellValue(row.getCell(11));
//                try {
//                    rol.setIdRol(Integer.parseInt(rolStr));
//                } catch (Exception e) {
//                    rol.setIdRol(0);
//                }
//                usuario.setRol(rol);
//
//                // Evitar filas completamente vacías
//                if (!estaFilaVacia(usuario)) {
//                    usuarios.add(usuario);
//                }
//
//                rowIndex++;
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return usuarios;
//    }
//
//    private String getCellValue(Cell cell) {
//        if (cell == null) {
//            return "";
//        }
//        return switch (cell.getCellType()) {
//            case STRING ->
//                cell.getStringCellValue().trim();
//            case NUMERIC -> {
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    yield cell.getDateCellValue().toString();
//                } else {
//                    double value = cell.getNumericCellValue();
//                    yield (value == Math.floor(value)) ? String.valueOf((long) value) : String.valueOf(value);
//                }
//            }
//            case BOOLEAN ->
//                String.valueOf(cell.getBooleanCellValue());
//            case FORMULA ->
//                cell.getCellFormula();
//            default ->
//                "";
//        };
//    }
//
//    private boolean esEncabezado(Row row) {
//        String[] posiblesCampos = {
//            "username", "nombre", "apellidopat", "apellidomat",
//            "email", "password", "fecha", "sexo", "telefono", "celular", "curp", "rol"
//        };
//
//        // Leer las primeras celdas como texto y comparar
//        for (int i = 0; i < 5 && i < row.getLastCellNum(); i++) {
//            String valor = getCellValue(row.getCell(i)).toLowerCase();
//            for (String campo : posiblesCampos) {
//                if (valor.contains(campo)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean estaFilaVacia(Usuario usuario) {
//        return (usuario.getUserName() == null || usuario.getUserName().isBlank())
//                && (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isBlank())
//                && (usuario.getEmail() == null || usuario.getEmail().isBlank());
//    }
//
////------------------------------------------------------USUARIODETAIL----------------------------------------------------------
    @GetMapping("/detail/{id}")
    public String getUsuarioDetail(@PathVariable int id, Model model, HttpSession session) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login?expired=true";
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Result<Usuario>> responseUsuario = restTemplate.exchange(
                    URL + "/detail/" + id,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<Usuario>>() {
            }
            );
            ResponseEntity<Result<Rol>> responseRoles = restTemplate.exchange(
                    URL + "/roles",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<Rol>>() {
            }
            );
            ResponseEntity<Result<Pais>> responsePaises = restTemplate.exchange(
                    URL + "/paises",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Result<Pais>>() {
            }
            );
            if (responseUsuario.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("usuario", responseUsuario.getBody().object);
                model.addAttribute("direccio", new Direccion());
            } else {
                model.addAttribute("usuario", new Usuario());
            }
            if (responseRoles.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("roles", responseRoles.getBody().objects);
            }
            if (responsePaises.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("paises", responsePaises.getBody().objects);
            }
            return "UsuarioDetail";
        } catch (Exception ex) {
            return "redirect:/login?forbidden=true";
        }
    }
//---------------------------------------------------------USUARIOFORM-----------------------------------------------------------

    @GetMapping("/add")
    public String ADD(Model model) {
        Usuario usuario = new Usuario();
        RestTemplate restTemplate = new RestTemplate();
        model.addAttribute("Usuario", usuario);
        ResponseEntity<Result<Rol>> responseEntityRol = restTemplate.exchange(
                URL + "/roles",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Rol>>() {
        });
        ResponseEntity<Result<Pais>> responseEntityPais = restTemplate.exchange(
                URL + "/paises",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Pais>>() {
        });
        if (responseEntityRol.getStatusCode().value() == 200) {
            model.addAttribute("roles", responseEntityRol.getBody().objects);
        }
        if (responseEntityPais.getStatusCode().value() == 200) {
            model.addAttribute("paises", responseEntityPais.getBody().objects);
        }
        return "UsuarioForm";
    }

    @PostMapping("/add")
    public String ADD(
            @ModelAttribute("Usuario") Usuario usuario,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("imagenFile") MultipartFile imagenFile
    ) {

        if (bindingResult.hasErrors()) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Result<Rol>> rolesResponse = restTemplate.exchange(
                    URL + "/roles",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Rol>>() {
            }
            );
            ResponseEntity<Result<Pais>> paisesResponse = restTemplate.exchange(
                    URL + "/paises",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<Pais>>() {
            }
            );
            model.addAttribute("roles", rolesResponse.getBody().objects);
            model.addAttribute("paises", paisesResponse.getBody().objects);
            int idPais = usuario.DireccionesJPA.get(0).getColoniaJPA().getMunicipioJPA().getEstadoJPA().getPaisJPA().getIdPais();
            if (idPais > 0) {
                ResponseEntity<Result<Estado>> estadosResponse = restTemplate.exchange(
                        URL + "/estados/" + idPais,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Estado>>() {
                }
                );
                model.addAttribute("estados", estadosResponse.getBody().objects);
            }
            int idEstado = usuario.DireccionesJPA.get(0).getColoniaJPA().getMunicipioJPA().getEstadoJPA().getIdEstado();
            if (idEstado > 0) {
                ResponseEntity<Result<Municipio>> municipiosResponse = restTemplate.exchange(
                        URL + "/municipios/" + idEstado,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Municipio>>() {
                }
                );
                model.addAttribute("municipios", municipiosResponse.getBody().objects);
            }
            int idMunicipio = usuario.DireccionesJPA.get(0).getColoniaJPA().getMunicipioJPA().getIdMunicipio();
            if (idMunicipio > 0) {
                ResponseEntity<Result<Colonia>> coloniasResponse = restTemplate.exchange(
                        URL + "/colonias/" + idMunicipio,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<Colonia>>() {
                }
                );
                model.addAttribute("colonias", coloniasResponse.getBody().objects);
            }
            return "UsuarioForm";
        }
        try {
            if (imagenFile != null && !imagenFile.isEmpty()) {
                usuario.setImagen(
                        Base64.getEncoder().encodeToString(imagenFile.getBytes())
                );
            } else {
                usuario.setImagen(null);
            }
        } catch (IOException ex) {
            redirectAttributes.addFlashAttribute("MensajeError", "Error al procesar la imagen");
            return "redirect:/usuario/add";
        }
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Usuario> request = new HttpEntity<>(usuario, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                    URL + "/add",
                    HttpMethod.POST,
                    request,
                    Result.class
            );
            Result result = response.getBody();
            if (result != null && result.correct) {
                redirectAttributes.addFlashAttribute("MensajeExito", "Usuario agregado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("MensajeError",
                        "Error: " + (result != null ? result.errorMessage : "desconocido"));
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("MensajeError", "Error al comunicar con el servicio: " + ex.getMessage());
            return "redirect:/usuario/add";
        }
        return "redirect:/usuario";
    }
//---------------------------------------------------------DDLS DIRECCION-----------------------------------------------------------------

    @GetMapping("/Estados/{IdPais}")
    public Result EstadosGETBYIDPais(@PathVariable int IdPais, Model model) {
        Result result = new Result();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Estado>> responseEntityEstado = restTemplate.exchange(
                URL + "/estados ",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Estado>>() {
        });
        if (responseEntityEstado.getStatusCode().value() == 200) {
            model.addAttribute("estados", responseEntityEstado.getBody().objects);
            System.out.println(EstadosGETBYIDPais(IdPais, model));
        }
        return result;
    }

    @GetMapping("/Municipios/{IdEstado}")
    public Result MunicipiosGETBYIDEstado(@PathVariable int IdEstado, Model model) {
        Result result = new Result();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Municipio>> responseEntityMunicipio = restTemplate.exchange(
                URL + "/municipios ",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Municipio>>() {
        });
        if (responseEntityMunicipio.getStatusCode().value() == 200) {
            model.addAttribute("estados", responseEntityMunicipio.getBody().objects);
            System.out.println(EstadosGETBYIDPais(IdEstado, model));
        }
        return result;
    }

    @GetMapping("/Colonias/{IdMunicipio}")
    public Result ColoniasGETBYIDMunicipio(@PathVariable int IdMunicipio, Model model) {
        Result result = new Result();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Colonia>> responseEntityColonia = restTemplate.exchange(
                URL + "/colonias ",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Colonia>>() {
        });
        if (responseEntityColonia.getStatusCode().value() == 200) {
            model.addAttribute("estados", responseEntityColonia.getBody().objects);
            System.out.println(EstadosGETBYIDPais(IdMunicipio, model));
        }
        return result;
    }
//-----------------------------------------------------UPDATE USUARIO-----------------------------------------------------------------

    @PostMapping("/update")
    public String updateUsuario(@ModelAttribute("Usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Usuario> request = new HttpEntity<>(usuario, headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                    URL + "/update",
                    HttpMethod.PUT,
                    request,
                    Result.class
            );
            Result result = response.getBody();
            if (result != null && result.correct) {
                redirectAttributes.addFlashAttribute("mensajeExito",
                        "Se actualizaron los datos de " + usuario.getUserName());
            } else {
                String errorMsg = (result != null) ? result.errorMessage : "Error desconocido";
                redirectAttributes.addFlashAttribute("mensajeError",
                        "Error al actualizar datos: " + errorMsg);
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "Error al conectar con el servicio: " + ex.getMessage());
        }
        return "redirect:/usuario/detail/" + usuario.getIdUsuario();
    }
////    -------------------------------------------------------------ADD DIRECCION-------------------------------------------------

    @PostMapping("/direccion/add/{idUsuario}")
    public String addDireccion(
            @PathVariable int idUsuario,
            @ModelAttribute("direccion") Direccion direccion,
            RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Direccion> request = new HttpEntity<>(direccion, httpHeaders);
            ResponseEntity<Result> response = restTemplate.exchange(
                    URL + "/add-direccion/" + idUsuario,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Result>() {
            });
            if (response.getBody().correct) {
                redirectAttributes.addFlashAttribute("Mensaje Exito", "Direccion agregada correctamenete");
            } else {
                redirectAttributes.addFlashAttribute("Mensaje Error", "Erro al agregar la direccion"
                        + response.getBody().errorMessage);
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("Mensaje Error", "Error al conectar con el servicio" + ex.getLocalizedMessage());
        }
        return "redirect:/usuario/detail/" + idUsuario;
    }
////--------------------------------------------------------UPDATE DIRECCION-------------------------------------------------

    @PostMapping("/direccion/update/{idUsuario}")
    public String UpdareDireccion(@PathVariable int idUsuario,
            @ModelAttribute("direccion") Direccion direccion,
            RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Direccion> request = new HttpEntity<>(direccion, httpHeaders);
            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    URL + "/update-direccion/" + idUsuario,
                    HttpMethod.PUT,
                    request,
                    new ParameterizedTypeReference<Result>() {
            });
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("Mensaje Error", "Error al actualizar la direccion"
                    + ex.getLocalizedMessage());
        }
        return "redirect:/usuario/detail/" + idUsuario;
    }
////------------------------------------------------------ELIMINAR USUARIO------------------------------------------------------

    @PostMapping("/delete")
    public String eliminarUsuario(@RequestParam int idUsuario, RedirectAttributes redirectAttributes) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Result> response = restTemplate.exchange(
                    URL + "/delete-usuario/" + idUsuario,
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Result.class
            );

            Result result = response.getBody();

            if (result != null && result.correct) {
                redirectAttributes.addFlashAttribute("mensajeExito", "Usuario eliminado correctamente");
            } else {
                String errorMsg = (result != null) ? result.errorMessage : "Error desconocido";
                redirectAttributes.addFlashAttribute("mensajeError", "Error al eliminar usuario: " + errorMsg);
            }

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al conectar con el servicio: " + ex.getMessage());
            ex.printStackTrace();
        }

        return "redirect:/usuario";
    }

////    ---------------------------------------------------GETDIRECCIONBYIDDIRECCION---------------------------------------------------
    @GetMapping("direccion/{idDireccion}")
    public String GetDireccionByIdDireccion(@PathVariable int idDireccion,
            Model model,
            RedirectAttributes redirectAttributes) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    URL + "/direccion" + idDireccion,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result>() {
            });
            Result result = responseEntity.getBody();
            if (result.correct) {
                Direccion direccion = (Direccion) result.object;
                model.addAttribute("direccion", direccion);
                return "usuario/editar";
            } else {
                redirectAttributes.addFlashAttribute("MensajeError", result.errorMessage);
                return "redirect:/usuario/list";
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("MensajeError",
                    "Error al conectar con el servicio: " + ex.getLocalizedMessage());
            return "redirect: /usuario/detail";
        }
    }
//----------------------------------------------DIRECCION DELETE-------------------------------------------------------

    @PostMapping("/direccion/delete/{idUsuario}")
    public String deleteDireccion(@PathVariable int idUsuario,
            @RequestParam int idDireccion,
            RedirectAttributes redirectAttributes) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Result> response = restTemplate.exchange(
                    URL + "/delete-direccion/" + idDireccion,
                    HttpMethod.DELETE,
                    HttpEntity.EMPTY,
                    Result.class
            );
            Result result = response.getBody();

            if (result != null && result.correct) {
                redirectAttributes.addFlashAttribute("MensajeExito", "Dirección eliminada correctamente");
            } else {
                String errorMsg = (result != null) ? result.errorMessage : "Error desconocido";
                redirectAttributes.addFlashAttribute("MensajeError", "No se pudo eliminar la dirección: " + errorMsg);
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("MensajeError", "Error al conectar con el servicio: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "redirect:/usuario/detail/" + idUsuario;
    }
////-------------------------------------IMAGEN UPDATE----------------------------------------------------------

    @PostMapping("/update-imagen")
    public String UpdateImagen(@RequestParam("idUsuario") int idUsuario, @RequestParam("imagen") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se seleccionó ningún archivo");
            return "redirect:/usuario/detail/" + idUsuario;
        }
        try {
            String imagenBase64 = Base64.getEncoder().encodeToString(file.getBytes());
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            usuario.setImagen(imagenBase64);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Usuario> request = new HttpEntity<>(usuario, headers);
            String url = URL + "/update-imagen/" + idUsuario;
            ResponseEntity<Result> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    request,
                    Result.class
            );
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al procesar la imagen: " + ex.getMessage());
            ex.printStackTrace();
        }
        return "redirect:/usuario/detail/" + idUsuario;
    }
////---------------------------------------------------BUCADOR DINAMICO----------------------------------------------------

    @PostMapping()
    public String BuscarUsuario(@ModelAttribute("Usuario") Usuario usuario, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result> response = restTemplate.exchange(
                URL + "/busqueda",
                HttpMethod.POST,
                new HttpEntity<>(usuario),
                new ParameterizedTypeReference<Result>() {
        }
        );
        Result result = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        List<Usuario> usuariosConvertidos
                = mapper.convertValue(result.objects, new TypeReference<List<Usuario>>() {
                });
        ResponseEntity<Result> responseRoles = restTemplate.exchange(
                URL + "/roles",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {
        }
        );
        List<Rol> roles = mapper.convertValue(responseRoles.getBody().objects,
                new TypeReference<List<Rol>>() {
        });
        model.addAttribute("usuarios", usuariosConvertidos);
        model.addAttribute("roles", roles);
        model.addAttribute("Usuario", usuario);
        return "UsuarioIndex";
    }
//-----------------------------------------UPDATE STATUS----------------------------------------------------------------

    @PostMapping("/toggleStatus")
    public String toggleStatus(@RequestParam int idUsuario,
            @RequestParam(required = false) String status) {
        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();
        try {
            String url = "http://localhost:8080/api/usuario/update-status/" + idUsuario + "/" + status;
            restTemplate.put(url, null);  // llama al API REST

        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return "redirect:/usuario";
    }

//----------------------------------------CARGA MASIVA-----------------------------------------   
//    @GetMapping("/carga")
//    public String mostrarCargaMasiva(Model model) {
//        return "UsuarioCargaMasiva";
//    }
//
//    @PostMapping("/carga")
//    public String CargarArchivo(@RequestParam("archivo") MultipartFile archivo, Model model) {
//        if (archivo.isEmpty()) {
//            model.addAttribute("Mensaje Error", "Debe seleccionar un archivo");
//            return "UsuarioCargaMasiva";
//        }
//        try {
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
//            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            ByteArrayResource
//        } catch (Exception e) {
//        }
//        return "UsuarioCargaMasiva";
//    }
}
