package com.snow.storeapi.controller;

import com.snow.storeapi.entity.SizeGroup;
import com.snow.storeapi.service.ISizeGroupService;
import com.snow.storeapi.service.IVersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/size-group")
@RequiredArgsConstructor
public class SizeGroupController {
    private final ISizeGroupService sizeGroupService;
    private final IVersionService versionService;
    @GetMapping("/all")
    public List list() {
        List<SizeGroup> sizeGroups = sizeGroupService.list();
        return sizeGroups;
    }

    @PostMapping("")
    @Transactional(rollbackFor = Exception.class)
    public SizeGroup create(@Valid @RequestBody SizeGroup sizeGroup) {
        sizeGroupService.save(sizeGroup);
        versionService.addOne("sizeGroup");
        return sizeGroup;
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public void delete(@PathVariable Integer id) {
        versionService.addOne("sizeGroup");
        sizeGroupService.removeById(id);
    }

    @PatchMapping("")
    @Transactional(rollbackFor = Exception.class)
    public void update(@Valid @RequestBody SizeGroup sizeGroup) {
        versionService.addOne("sizeGroup");
        sizeGroupService.updateById(sizeGroup);
    }
}
