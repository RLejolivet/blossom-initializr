package com.blossomproject.showcase.initializr;

import com.blossomproject.showcase.initializr.model.PACKAGING_MODE;
import com.blossomproject.showcase.initializr.model.ProjectConfiguration;
import com.blossomproject.showcase.initializr.model.SOURCE_LANGUAGE;
import com.blossomproject.showcase.initializr.model.ProjectGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.blossomproject.showcase.initializr.model.Initializr;
import javax.servlet.http.HttpServletResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
@Controller
@RequestMapping("/initializr")
public class InitializrController {
  private final Logger logger = LoggerFactory.getLogger(InitializrController.class);

  private final Initializr initializr;
  private final ProjectGenerator projectGenerator;
  private final ObjectMapper mapper;
  private final BulkProcessor bulkProcessor;

  @Autowired
  public InitializrController(Initializr initializr, ProjectGenerator projectGenerator,
    ObjectMapper mapper, BulkProcessor bulkProcessor) {
    this.initializr = initializr;
    this.projectGenerator = projectGenerator;
    this.mapper = mapper;
    this.bulkProcessor = bulkProcessor;
  }

  @GetMapping
  public ModelAndView main(Model model) {
    model.addAttribute("project", new ProjectConfiguration("blossom-starter-basic"));
    model.addAttribute("initializr", initializr);
    model.addAttribute("packagingModes", PACKAGING_MODE.values());
    model.addAttribute("sourceLanguages", SOURCE_LANGUAGE.values());

    return new ModelAndView("initializr");
  }

  @PostMapping(produces = "application/zip")
  public void generate(@ModelAttribute("form") ProjectConfiguration projectConfiguration,
    HttpServletResponse res)
    throws Exception {
    if (projectConfiguration.getDependencies().size() == 0) {
      projectConfiguration.getDependencies().add("blossom-starter-basic");
    }
    res.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + projectConfiguration.getArtifactId() + ".zip\"");
    projectGenerator.generateProject(projectConfiguration, res.getOutputStream());

    ObjectNode document = mapper.valueToTree(projectConfiguration);
    document.put("timestamp", System.currentTimeMillis());

    bulkProcessor.add(new IndexRequest("generations","generation").source(mapper.writeValueAsString(document)));
  }
}
