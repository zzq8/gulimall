package com.zzq.gulimall.search.service;

import com.zzq.gulimall.search.vo.SearchParam;
import com.zzq.gulimall.search.vo.SearchResult;


public interface MallSearchService {
    SearchResult search(SearchParam param);
}
