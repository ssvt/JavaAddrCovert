package rapiragroup.javaaddrcovert.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyConfig {
    private String folder_tables_legacy;
    private String folder_tables_segwit;
    private String folder_tables_native_segwit;

    private BigDecimal number_of_generated_mnemonics;
    private Integer num_child_addresses;

    private String path_m0_x;
    private String path_m1_x;
    private String path_m0_0_x;
    private String path_m0_1_x;
    private String path_m44h_0h_0h_0_x;
    private String path_m44h_0h_0h_1_x;
    private String path_m49h_0h_0h_0_x;
    private String path_m49h_0h_0h_1_x;
    private String path_m84h_0h_0h_0_x;
    private String path_m84h_0h_0h_1_x;

    private String chech_equal_bytes_in_adresses;
    private String save_generation_result_in_file;

    private String static_words_generate_mnemonic;

    private Integer cuda_grid;
    private Integer cuda_block;
}
