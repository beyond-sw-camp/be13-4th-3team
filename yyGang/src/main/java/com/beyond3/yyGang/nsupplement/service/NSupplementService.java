package com.beyond3.yyGang.nsupplement.service;

import com.beyond3.yyGang.handler.exception.NSupplementException;
import com.beyond3.yyGang.handler.exception.OrderException;
import com.beyond3.yyGang.handler.exception.UserException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.hfunction.HFunctionName;
import com.beyond3.yyGang.hfunction.HFunctionalCategory;
import com.beyond3.yyGang.hfunction.repository.HFunctionalCategoryRepository;
import com.beyond3.yyGang.hfunction.HFunctionalItem;
import com.beyond3.yyGang.hfunction.repository.HFunctionalRepository;
import com.beyond3.yyGang.ingredient.domain.Ingredient;
import com.beyond3.yyGang.ingredient.domain.IngredientCategory;
import com.beyond3.yyGang.ingredient.repository.IngredientCategoryRepository;
import com.beyond3.yyGang.ingredient.domain.IngredientName;
import com.beyond3.yyGang.ingredient.repository.IngredientRepository;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.nsupplement.dto.NSupplementModifyDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDto;
import com.beyond3.yyGang.nsupplement.repository.NSupplementRepository;
import com.beyond3.yyGang.nsupplement.dto.NSupplementRegisterDto;
import com.beyond3.yyGang.user.domain.Role_name;
import com.beyond3.yyGang.user.domain.User;
import com.beyond3.yyGang.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NSupplementService {

    private final UserRepository userRepository;
    private final NSupplementRepository nsupplementRepository;
    private final IngredientRepository ingredientRepository;
    private final HFunctionalRepository hFunctionalRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final HFunctionalCategoryRepository hFunctionalCategoryRepository;


    // 특정 사용자가 등록한 상품 리스트를 확인
    @Transactional
    public List<NSupplementResponseDto> getNSupplementsBySeller(String email) {

        // user 역할 확인
        User user = extractedUser(email);

        // 등록한 상품 리스트 확인
        List<NSupplement> products = nsupplementRepository.findBySeller(user);

//        // 등록한 상품이 없으면 예외 던져버려 -> 아니면 빈 페이지 보여줄까?
//        if(products.isEmpty()){
//            throw new UserException(UserExceptionMessage.NO_PRODUCTS_FOR_SELLER);
//        }

        List<NSupplementResponseDto> result = new ArrayList<>();

        for (NSupplement product : products) {
            NSupplementResponseDto nSupplementResponseDto = getNSupplementResponseDto(product);

            result.add(nSupplementResponseDto);
        }

        return result;
    }

    @Transactional
    public List<NSupplementRegisterDto> getAllNSupplements() {
        return nsupplementRepository.findAll().stream()
                .map(NSupplement::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    // 상품 등록
    public void registerProduct(String email, NSupplementRegisterDto nSupplementRegisterDto) {

        // 역할이 SELLER인지 확인
        User seller = extractedUser(email);

        // 이미 등록된 이름이라면 중복 등록 안되게 설정 -> 굳이?
//        if(!nsupplementRepository.findByProductName(nSupplementRegisterDto.getProductName()).isEmpty()){
//            throw new OrderException(ExceptionMessage.PRODUCT_ALREADY_EXISTS);
//        }

        // 상품 먼저 등록 -> 판매자 이름으로 등록
        NSupplement nSupplement = nSupplementRegisterDto.toEntity(seller);
        nsupplementRepository.findByProductName(nSupplementRegisterDto.getProductName());
        NSupplement savedProduct = nsupplementRepository.save(nSupplement);

        // 상품에 등록된 성분 카테고리에 관련된 내용 입력
        saveIngredientCategory(nSupplementRegisterDto.getIngredients(), savedProduct);
        // 상품에 등록된 건강 카테고리에 관련된 내용 입력
        saveHFunctionalCategory(nSupplementRegisterDto.getHFunctionalItems(), savedProduct);
    }

    @Transactional
    // 상품 삭제 서비스 로직
    public void deleteProduct(String email, Long nSupplementId) {
        // 사용자 확인
        User seller = extractedUser(email);

        // Seller가 nSupplementId의 상품을 등록했다면 해당 상품 가져오기
        NSupplement product = validateSeller(seller, nSupplementId);

        // 상품 삭제의 경우 -> ingredientCategory, HFunctioncalCategory에서 각각 제거가 먼저 되어야 함
        deleteIngredientCategory(product);
        deleteHFunctionalCategory(product);

        // 이후 NSupplement에서 삭제 가능
        nsupplementRepository.deleteById(nSupplementId);
    }

    @Transactional
    // 상품 수정 서비스 로직
    public void modifyProduct(String email, Long nSupplementId, NSupplementModifyDto dto) {
        User seller = extractedUser(email);

        // Seller가 nSupplementId의 상품을 등록했다면 해당 상품 가져오기
        NSupplement product = validateSeller(seller, nSupplementId);

        product.updateNSupplement(dto);

        if(!dto.getIngredients().isEmpty()){
            // 수정하려는 값이 존재하는 경우, 기존 값들을 지우고 업데이트
            deleteIngredientCategory(product);
            saveIngredientCategory(dto.getIngredients(), product);
        }
        if(!dto.getHFunctionalItems().isEmpty()){
            deleteHFunctionalCategory(product);
            saveHFunctionalCategory(dto.getHFunctionalItems(), product);
        }

    }

    // IngredientCategory에서 특정 상품 삭제
    private void deleteIngredientCategory(NSupplement product) {
        List<IngredientCategory> inCategoryList = ingredientCategoryRepository.findBynSupplement(product);
        ingredientCategoryRepository.deleteAll(inCategoryList);
    }

    // HFunctinoalCategory에서 특정 상품 삭제
    private void deleteHFunctionalCategory(NSupplement product) {
        List<HFunctionalCategory> hfuncCategoryList = hFunctionalCategoryRepository.findBynSupplement(product);
        hFunctionalCategoryRepository.deleteAll(hfuncCategoryList);
    }

    // ingredientCategoryRepository에 성분 태그 저장
    private void saveIngredientCategory(List<IngredientName> ingredientNames, NSupplement product) {
        // 상품에 등록된 성분 카테고리에 관련된 내용 입력
        for (IngredientName ingredientName : ingredientNames) {

            Ingredient ingredient = ingredientRepository.findByingredientName(ingredientName)
                    .orElseThrow(() -> new UserException(ExceptionMessage.INGREDIENT_NOT_FOUND));

            IngredientCategory ingredientCategory = new IngredientCategory(product, ingredient);
            ingredientCategoryRepository.save(ingredientCategory);
        }
    }

    // hFunctionalCategoryRepository에 건강 기능 태그 저장
    private void saveHFunctionalCategory(List<HFunctionName> hFunctionNames, NSupplement product) {
        // 상품에 등록된 성분 카테고리에 관련된 내용 입력
        for (HFunctionName hFunctionalItem : hFunctionNames) {

            HFunctionalItem hitem = hFunctionalRepository.findByHealthName(hFunctionalItem)
                    .orElseThrow(() -> new UserException(ExceptionMessage.HEALTH_FUNCTIONAL_ITEM_NOT_FOUND));

            HFunctionalCategory hFunctionalCategory = new HFunctionalCategory(product, hitem);
            hFunctionalCategoryRepository.save(hFunctionalCategory);
        }
    }

    // Seller가 가지고 있는 상품 리스트 안에 해당 상품이 있는지 확인
    private NSupplement validateSeller(User seller, Long nSupplementId){
        // Seller가 등록한 상품들 조회
        List<NSupplement> productList = nsupplementRepository.findBySeller(seller);

        // nSupplementId 아이디의 상품이 존재하는지 확인
        NSupplement product = nsupplementRepository.findByproductId(nSupplementId)
                .orElseThrow(() -> new UserException(ExceptionMessage.CANNOT_FOUND_PRODUCTS));

        // 삭제할 상품이 없거나 삭제할 상품 아이디가 SELLER가 등록한 상품이 아닌 경우 -> 예외 던지게
        if (productList.isEmpty() || !productList.contains(product)) {
            throw new UserException(ExceptionMessage.NO_REGISTERED_PRODUCTS);
        }
        return product;
    }

    // 사용자 추출 -> 역할이 SELLER일 때만
    private User extractedUser(String email) {
        // 사용자 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // user의 역할이 SELLER인 경우만 해당 작업 가능
        if (!user.getRole().equals(Role_name.SELLER)) {
            throw new UserException(ExceptionMessage.ONLY_SELLER_CAN_ACCESS);
        }
        return user;
    }

    // 상품 단건 조회
    @Transactional
    public NSupplementResponseDto getNSupplement(Long nSupplementId) {

        NSupplement nSupplement = nsupplementRepository.findByproductId(nSupplementId)
                .orElseThrow(() -> new NSupplementException(ExceptionMessage.CANNOT_FOUND_PRODUCTS));

        return getNSupplementResponseDto(nSupplement);
    }

    private NSupplementResponseDto getNSupplementResponseDto(NSupplement nSupplement) {
        NSupplementResponseDto nSupplementResponseDto = new NSupplementResponseDto(nSupplement);

        List<IngredientName> ingredients = ingredientCategoryRepository.findBynSupplement(nSupplement)
                .stream().map(n -> n.getIngredient().getIngredientName())
                .toList();

        List<HFunctionName> hFunctionNames = hFunctionalCategoryRepository.findBynSupplement(nSupplement)
                .stream().map(n -> n.getHFunctionalItem().getHealthName())
                .toList();

        nSupplementResponseDto.setIngredients(ingredients);
        nSupplementResponseDto.setHealthNames(hFunctionNames);
        return nSupplementResponseDto;
    }
}