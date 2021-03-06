package com.weibomate.web

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.PathVariable
import org.apache.commons.codec.binary.Base64

@Controller
@RequestMapping(Array("/search"))
class SearchController {
  val logger = LoggerFactory.getLogger(classOf[SearchController])
  @Autowired var searchService: StatusService = _

  @RequestMapping(method = Array(RequestMethod.POST))
  def search(@RequestParam("keyword") keyword: String) = {
    "redirect:search/%s".format(Base64.encodeBase64URLSafeString(keyword.getBytes("UTF-8")))
  }

  @RequestMapping(value=Array("/{keyword}"), method = Array(RequestMethod.GET))
  def show(@PathVariable("keyword") keyword: String, modelMap: ModelMap) = {
    val keyword2 = new String(Base64.decodeBase64(keyword), "UTF-8")
    logger.info("------------get search keyword: %s.-----------".format(keyword2))
    val tweets = searchService.search(keyword2)
    tweets.foreach((doc) => logger.info("---Weibo: id: %s; text: %s; picture: %s".format(doc.getId(), doc.getText(), doc.getThumbnailPic())))
    modelMap.addObject("tweets", tweets)
    "search"
  }
}
