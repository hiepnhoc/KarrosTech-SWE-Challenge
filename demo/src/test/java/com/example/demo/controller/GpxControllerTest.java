package com.example.demo.controller;

import com.example.demo.dao.GpxRepo;
import com.example.demo.model.latestTrackView;
import com.example.demo.model.responseModel;
import com.example.demo.model.gpx;
import com.example.demo.service.GpxService;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.util.*;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = GpxController.class)
@WithMockUser
public class GpxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GpxService gpxService;

    @MockBean
    private GpxRepo gpxRepo;

    @Test
    public void verifyUploadGpx() throws Exception {

        String s="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"OruxMaps v.7.1.6 Donate\">\n" +
                "\t<metadata>\n" +
                "\t\t<name>test</name>\n" +
                "\t\t<desc>a</desc>\n" +
                "\t\t<author></author>\n" +
                "\t\t<link href=\"http://www.oruxmaps.com\">\n" +
                "\t\t\t<text>OruxMaps</text>\n" +
                "\t\t</link>\n" +
                "\t\t<time>2017-10-22T09:41:33Z</time>\n" +
                "\t</metadata>\n" +
                "\t<wpt lat=\"42.2205377\" lon=\"-1.4564538\">\n" +
                "\t\t<name>Sorteamos por arriba</name>\n" +
                "\t\t<sym>/static/wpt/Waypoint</sym>\n" +
                "\t</wpt>\n" +
                "\t<trk>\n" +
                "\t\t<trkseg>\n" +
                "\t\t\t<trkpt lat=\"42.2208895\" lon=\"-1.4580696\">\n" +
                "\t\t\t\t<ele>315.86</ele>\n" +
                "\t\t\t\t<time>2017-10-22T09:41:38Z</time>\n" +
                "\t\t\t</trkpt>\n" +
                "\t\t</trkseg>\n" +
                "\t</trk>\n" +
                "</gpx>";

        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.gpx", "application/gpx+xml", s.getBytes());
        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.multipart("/gpx/upload")
                .file(firstFile)
                .param("username", "hiepln"))
                .andExpect(status().isOk()).andReturn();

        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertNotNull(result.getResponse().getContentAsString());

    }

    @Test
    public void verifyGetLastestTrackByUsername() throws Exception {
        responseModel responseModel=new responseModel();
        latestTrackView lastestTrackView1=new latestTrackView(1L,"Nevada","american");
        latestTrackView lastestTrackView2=new latestTrackView(2L,"Newyork","american");
        List<latestTrackView> lastestTrackViewList=new ArrayList<latestTrackView>();
        lastestTrackViewList.add(lastestTrackView1);
        lastestTrackViewList.add(lastestTrackView2);

        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        responseModel.setResult_code(0);
        responseModel.setMessage("OK");
        responseModel.setData(lastestTrackViewList);
        when(gpxService.getLastestTrackByUserName(Mockito.anyMap())).thenReturn(Map.of("status", 200, "data", responseModel));

        MvcResult result =mockMvc.perform(MockMvcRequestBuilders.get("/gpx/lastesttrack/hiepln")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2))).andReturn();

        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void verifyGetDetail() throws Exception {
        String expect="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" version=\"1.1\" creator=\"OruxMaps v.7.1.6 Donate\">\n" +
                "\t<metadata>\n" +
                "\t\t<name>test</name>\n" +
                "\t\t<desc>a</desc>\n" +
                "\t\t<author></author>\n" +
                "\t\t<link href=\"http://www.oruxmaps.com\">\n" +
                "\t\t\t<text>OruxMaps</text>\n" +
                "\t\t</link>\n" +
                "\t\t<time>2017-10-22T09:41:33Z</time>\n" +
                "\t</metadata>\n" +
                "\t<wpt lat=\"42.2205377\" lon=\"-1.4564538\">\n" +
                "\t\t<name>Sorteamos por arriba</name>\n" +
                "\t\t<sym>/static/wpt/Waypoint</sym>\n" +
                "\t</wpt>\n" +
                "\t<trk>\n" +
                "\t\t<trkseg>\n" +
                "\t\t\t<trkpt lat=\"42.2208895\" lon=\"-1.4580696\">\n" +
                "\t\t\t\t<ele>315.86</ele>\n" +
                "\t\t\t\t<time>2017-10-22T09:41:38Z</time>\n" +
                "\t\t\t</trkpt>\n" +
                "\t\t</trkseg>\n" +
                "\t</trk>\n" +
                "</gpx>";

        responseModel responseModel=new responseModel();

        gpx gpx=new gpx();
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper=new XmlMapper(module);
        gpx= xmlMapper.readValue(expect, gpx.class);

        responseModel.setReference_id(UUID.randomUUID().toString());
        responseModel.setDate_time(new Timestamp(new Date().getTime()));
        responseModel.setResult_code(0);
        responseModel.setMessage("OK");
        responseModel.setData(Map.of("result", gpx));
        when(gpxService.getById(1L)).thenReturn(Map.of("status", 200, "data", responseModel));

        MvcResult result=mockMvc.perform(MockMvcRequestBuilders.get("/gpx/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Assert.assertEquals(200, result.getResponse().getStatus());
        Assert.assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void verifyDownloadGpx() throws Exception {
        Mockito.when(gpxService.downloadFile(Mockito.anyLong())).thenReturn(Mockito.anyMap());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/gpx/download/1")
                .contentType("application/gpx+xml")).andExpect(MockMvcResultMatchers.status().is(404)).andReturn();
        Assert.assertEquals(404, result.getResponse().getStatus());
        Assert.assertEquals(0, result.getResponse().getContentAsByteArray().length);
    }
}
