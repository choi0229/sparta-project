package com.sparta.project.service;

import com.sparta.project.common.ServiceException;
import com.sparta.project.common.ServiceExceptionCode;
import com.sparta.project.entity.Category;
import com.sparta.project.entity.Product;
import com.sparta.project.repository.CategoryJpaRepository;
import com.sparta.project.repository.CategoryQueryRepository;
import com.sparta.project.repository.projection.CategorySalesProjection;
import com.sparta.project.repository.projection.ProductSalesProjection;
import com.sparta.project.service.dto.CategoryRequestDto;
import com.sparta.project.service.dto.CategoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    @Transactional
    public void createCategory(CategoryRequestDto request){
        Boolean categoryHas = categoryJpaRepository.existsByName(request.getName());
        if(categoryHas){
            throw new ServiceException(ServiceExceptionCode.ALREADY_EXISTS_CATEGORY);
        }
        Category parentCategory = null;
        if(request.getParentId() != null){
            parentCategory = categoryJpaRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY));
        }
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .parent(parentCategory)
                .build();

        categoryJpaRepository.save(category);
    }

    @Transactional
    public void updateCategory(Long id, CategoryRequestDto request){
        // 카테고리 가져오기
        Category category = categoryJpaRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY));

        // 카테고리 이름 중복체크
        Boolean categoryHas = categoryJpaRepository.existsByNameAndIdNot(request.getName(), id);
        if(categoryHas){
            throw new ServiceException(ServiceExceptionCode.ALREADY_EXISTS_CATEGORY);
        }

        // 카테고리 부모 가져오기
        Category parentCategory = null;
        if(request.getParentId() != null){
            parentCategory = categoryJpaRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY));
        }
        category.updateCategory(
                request.getName(),
                parentCategory,
                request.getDescription()
        );
    }

    @Transactional
    public void deleteCategory(Long id){
        Category category = categoryJpaRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY));

        //if(!category.getChildren().isEmpty()){
        //    throw new ServiceException(ServiceExceptionCode.DELETE_NOT_ALLOWED);
        //}
        //if(!category.getProducts().isEmpty()){
        //   throw new ServiceException(ServiceExceptionCode.DELETE_NOT_ALLOWED);
        //}

        if(categoryQueryRepository.hasChild(id) || categoryQueryRepository.hasProduct(id)){
            throw new ServiceException(ServiceExceptionCode.DELETE_NOT_ALLOWED);
        }
        categoryJpaRepository.deleteById(id);
    }

    @Transactional
    public List<CategoryResponseDto> getCategoryTree(){
        List<Category> categoryList = categoryQueryRepository.findRootCategories();
        return categoryList.stream()
                .map(CategoryResponseDto::fromWithChildren)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ProductSalesProjection> getTopProducts(Long id){
        Category category = categoryJpaRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY));

        return categoryQueryRepository.getTopProducts(id, 10);
    }

    @Transactional
    public List<CategorySalesProjection> getTopCategories(int limit){
        return categoryQueryRepository.getTopCategories(limit);
    }
}
